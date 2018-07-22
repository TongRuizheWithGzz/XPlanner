package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Service.Exception.HTTPRequestNotOKException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public class SpiderUtil {
    public static void isResponseOK(HttpResponse response, String website) {

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK)
            throw new HTTPRequestNotOKException(website + " response badly!");
    }
}
