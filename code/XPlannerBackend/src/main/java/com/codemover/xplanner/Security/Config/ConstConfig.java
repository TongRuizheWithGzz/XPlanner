package com.codemover.xplanner.Security.Config;

import org.springframework.beans.factory.annotation.Value;

public class ConstConfig {
    @Value("${oauth2.jaccount.clientID}")
    public  String clientID;

    @Value("${oauth2.jaccount.clientSecret}")
    public  String clientSecret;

    @Value("${oauth2.jaccount.authorizationUrl}")
    public  String authorizationUrl;

    @Value("${oauth2.jaccount.redirectUrl}")
    public  String redirectUrl;

    @Value("${oauth2.jaccount.accessTokenUrl}")
    public  String accessTokenUrl;

    @Value("${oauth2.jaccount.refreshTokenUrl}")
    public  String refreshTokenUrl;

    @Value("${oauth2.jaccount.resource.profileUrl}")
    public  String profileUrl;


    @Value("${oauth2.jaccount.resource.examsUrl}")
    public  String examsUrl;

    @Value("${oauth2.jaccount.resource.lessonsUrl}")
    public  String lessonsUrl;

    @Value("${oauth2.jaccount.scope}")
    public  String scope;
}
