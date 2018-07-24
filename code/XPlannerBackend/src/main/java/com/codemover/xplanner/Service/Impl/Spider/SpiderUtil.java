package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Service.Exception.HTTPRequestNotOKException;
import com.google.common.hash.Hashing;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import javax.validation.constraints.NotNull;

public class SpiderUtil {
    public static void isResponseOK(HttpResponse response, String website) {

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK)
            throw new HTTPRequestNotOKException(website + " response badly!");
    }

    public static void isResponseOK(int statusCode, String website) {
        if (statusCode != HttpStatus.SC_OK)
            throw new HTTPRequestNotOKException(website + " response badly!");
    }

    public static Integer hashCode(@NotNull String in) {
        return Hashing.
                murmur3_32().
                hashBytes(in.getBytes())
                .asInt();
    }

}
