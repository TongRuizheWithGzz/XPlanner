package com.codemover.xplanner.OAuth2Test;


import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)

@SpringBootTest
public class OAuth2ControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    @Test
    public void shouldGetJAccountLoginAlwaysBeOk() throws Exception {
        this.mvc.perform(get("/api/loginByJAccount/getJAccountLoginUrl"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.errMsg", is("loginByJAccount:ok")));
    }

    @Test
    public void GetCorrectUrlTest() throws OAuthSystemException {
        OAuthClientRequest request = OAuthClientRequest
                .authorizationLocation("https://jaccount.sjtu.edu.cn/oauth2/authorize")
                .setClientId("ajebOnLZZi7Uk7y3Jbze")
                .setRedirectURI("http://localhost:8082/api/loginByJAccount/authorize")
                .setResponseType("code")
                .setState("xyz")
                .setScope("lessons")
                .buildQueryMessage();
        String UrlForGetCode = request.getLocationUri();
        OAuthClientRequest request2 = OAuthClientRequest
                .authorizationLocation("https://jaccount.sjtu.edu.cn/oauth2/logout")
                .setClientId("ajebOnLZZi7Uk7y3Jbze")
                .setRedirectURI("https://baidu.com")
                .buildQueryMessage();
        String UrlForGetCode2 = request2.getLocationUri();
        System.out.println(UrlForGetCode);


        System.out.println(UrlForGetCode2);
    }


}
