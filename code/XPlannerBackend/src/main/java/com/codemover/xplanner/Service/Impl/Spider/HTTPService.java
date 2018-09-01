package com.codemover.xplanner.Service.Impl.Spider;

import java.io.IOException;

public interface HTTPService {
    String HttpGet(String url, String encoding)
            throws IOException;


    String HttpGetWithTimeout(String url, String encoding)
            throws Exception;
}


