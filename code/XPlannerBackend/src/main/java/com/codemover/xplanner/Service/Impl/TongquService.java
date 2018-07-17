package com.codemover.xplanner.Service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TongquService {

    @Value("${api.tongqu.url}")
    private String tongquApiUrl;

    public String getActsFromTongqu(Integer offset) {
        return null;

    }

    public String buildUrl(Integer offset, String orderBy) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tongquApiUrl)
                .queryParam("type", 0)
                .queryParam("status", 0)
                .queryParam("order", orderBy)
                .queryParam("offset", offset);

        return builder.toUriString();

    }
}
