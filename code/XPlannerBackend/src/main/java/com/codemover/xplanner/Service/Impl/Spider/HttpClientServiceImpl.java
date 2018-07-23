package com.codemover.xplanner.Service.Impl.Spider;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.*;
import javax.inject.Inject;

@Service
public class HttpClientServiceImpl implements HTTPService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private CloseableHttpClient cacheClient;
    private HttpCacheContext context;

    private Long globalTimeout;

    @Inject
    public HttpClientServiceImpl(@Value("${const.cacheHttpClient.maxObjectSize}") Integer maxObjectSize,
                                 @Value("${const.cacheHttpClient.maxCacheEntries}") Integer maxCacheEntries,
                                 @Value("${const.cacheHttpClient.connectTimeout}") Integer connectTimeout,
                                 @Value("${const.cacheHttpClient.socketTimeout}") Integer socketTimeout,
                                 @Value("${const.spider.timeout}") Long mills) {
        this.globalTimeout = mills;
        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(maxCacheEntries)
                .setMaxObjectSize(maxObjectSize)
                .setHeuristicCachingEnabled(true)
                .setNeverCacheHTTP10ResponsesWithQueryString(false)
                .setSharedCache(false)
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        cacheClient = CachingHttpClients.custom()
                .setCacheConfig(cacheConfig)
                .setDefaultRequestConfig(requestConfig)
                .build();
        context = HttpCacheContext.create();
    }


    @Override
    public String HttpGet(String url, String encoding)
            throws IOException {
        logger.info("GET: " + url);
        String result;
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = cacheClient.execute(httpGet, context);
        SpiderUtil.isResponseOK(response.getStatusLine().getStatusCode(), url);

        try {
            CacheResponseStatus responseStatus = context.getCacheResponseStatus();
            switch (responseStatus) {
                case CACHE_HIT:
                    logger.info("A response was generated from the cache with " +
                            "no requests sent upstream");
                    break;
                case CACHE_MODULE_RESPONSE:

                    logger.info("The response was generated directly by the " +
                            "caching module");
                    break;
                case CACHE_MISS:
                    logger.info("The response came from an upstream server");
                    break;
                case VALIDATED:
                    logger.info("The response was generated from the cache " +
                            "after validating the entry with the origin server");
                    break;
            }
            result = IOUtils.toString(response.getEntity().getContent(), encoding);
        } finally {
            response.close();
        }

        return result;
    }


    @Override
    public String HttpGetWithTimeout(String url, String encoding)
            throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = new Callable<Object>() {
            public Object call() throws IOException {
                return HttpGet(url, encoding);
            }
        };

        Future<Object> future = executor.submit(task);

        Object result = future.get(this.globalTimeout, TimeUnit.MILLISECONDS);

        future.cancel(true); // may or may not desire this

        return (String) result;

    }


}
