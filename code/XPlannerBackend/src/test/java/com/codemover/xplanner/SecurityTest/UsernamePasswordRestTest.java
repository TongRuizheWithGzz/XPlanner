package com.codemover.xplanner.SecurityTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UsernamePasswordRestTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;


    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    public void shouldGetUnauthorizedWithoutRole() throws Exception {
        List<String> urls = Arrays.asList(
                "/api",
                "/api/schedules",
                "/api/schedules/5",
                "/api/schedules/monthScheduleInfo",
                "/api/scheduleForDay"
        );
        for (String url : urls) {
            mvc.perform(get("/api/schedules/"))
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.errMsg", is("Authentication failed:Bad credentials")))
                    .andExpect(jsonPath("$.errno", is(1)));
        }
    }


    @Test
    @WithAnonymousUser
    public void canGetAuthorizedWithCorrectUsernameAndPassword()
            throws Exception {
        mvc.perform(post("/api/auth/loginByUsernamePassword")
                .param("username", "tongruizhe")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.errno", is(0)))
                .andExpect(jsonPath("$.errMsg", is("loginByUsernamePassword:ok")));

    }


}
