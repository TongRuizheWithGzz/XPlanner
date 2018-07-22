package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Exception.SpiderRequestParamException;
import com.codemover.xplanner.Service.Impl.Spider.TongquSpider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TongquSpiderTest {

    @Autowired
    private TongquSpider tongquSpider;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getOffset0AndSmallNumberSuccess() throws IOException {

        Collection response = tongquSpider.getInfoFromWebsite(0, 5);
        assertThat(response.size()).isEqualTo(5);

    }

    @Test
    public void getOffsetNotSeroAndSmallNumberSuccess() throws IOException {

        Collection response = tongquSpider.getInfoFromWebsite(8, 9);
        assertThat(response.size()).isEqualTo(9);

    }

    @Test(expected = SpiderRequestParamException.class)
    public void getOffsetNotSeroAndBigNumberFailed() throws IOException {
        Collection response = tongquSpider.getInfoFromWebsite(0, 15);

    }


}
