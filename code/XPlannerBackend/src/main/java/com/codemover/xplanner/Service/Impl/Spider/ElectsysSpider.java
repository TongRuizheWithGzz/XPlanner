package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Model.Entity.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("ElectsysSpider")
public class ElectsysSpider implements ISpider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${url.electsys.info}")
    private String EleUrl;


    @Value("${url.electsys.login}")
    private String EleLoginUrl;

    @Value("${url.electsys.allInfo}")
    private String EleAllInfoUrl;

    @Autowired
    private HTTPService httpService;


    private String website;

    public ElectsysSpider() {
        website = "教学信息服务网";

    }


    @Override
    @Async
    public CompletableFuture<Collection<Notification>> getInfoFromWebsite(Integer offset, Integer number)
            throws IOException {
        String allInfoXml = getAllInfo(EleAllInfoUrl);
        String patternForTitle = "<title>(.+?)</title>";
        String patternForLink = "<link>(.+?)</link>";
        String patternForPubDate = "<pubDate>(.+?)</pubDate>";
        Pattern pattern1 = Pattern.compile(patternForTitle);
        Pattern pattern2 = Pattern.compile(patternForLink);
        Pattern pattern3 = Pattern.compile(patternForPubDate);


        Matcher m1 = pattern1.matcher(allInfoXml);
        Matcher m2 = pattern2.matcher(allInfoXml);

        Matcher m3 = pattern3.matcher(allInfoXml);

        ArrayList<Notification> notifications = new ArrayList<>();
        Integer count = 0;
        Integer hasParsed = 0;
        Boolean continueFlag=false;
        m1.find();
        m2.find();
        while (m1.find() && m2.find() && m3.find()) {
            Notification notification = new Notification();
            continueFlag=false;
            try {
                if (count < offset) {
                    count++;
                    continueFlag=true;
                    continue;
                }
                if (hasParsed >= number)
                    break;

                notification.title = m1.group(1);

                String link = m2.group(1);

                String pubDate = m3.group(1);
                notification.start_time = pubDate.substring(0, pubDate.length() - 3);
                notification.end_time = notification.start_time;
                notification.imageUrl = "";
                notification.address = "";
                notification.website = website;
                notification.description = "";

                String detailHtml = getAllInfo(link);

                Document detailDoc = Jsoup.parse(detailHtml);


                Element table = detailDoc.select("table.main_r_co_fo").get(0);
                notification.description = table.text().substring(0, Integer.min(1024, table.text().length()));
                notification.setNotificationId(notification.hashCode());
                notifications.add(notification);
                ObjectMapper mapper = new ObjectMapper();
                String what = mapper.writeValueAsString(notification);
                logger.info("来自教务处的信息:here "+ what);
            } catch (IndexOutOfBoundsException e) {
                ObjectMapper mapper = new ObjectMapper();
                String what = mapper.writeValueAsString(notification);
                logger.info("来自教务处的信息:there "+what);
                notification.setNotificationId(notification.hashCode());
                notifications.add(notification);
            } catch (Exception e) {
                ObjectMapper mapper = new ObjectMapper();
                String what = mapper.writeValueAsString(notification);
                logger.info("来自教务处的信息: here there"+what);
                logger.warn(e.getMessage());
            } finally {
                if(!continueFlag)
                    hasParsed++;
            }
        }
        return CompletableFuture.completedFuture(notifications);
    }

    private String getAllInfo(String url) throws IOException {
        String resStr = httpService.HttpGet(url, "gb2312");
        return resStr;
    }


}
