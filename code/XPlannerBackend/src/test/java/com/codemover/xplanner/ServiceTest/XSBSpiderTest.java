package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.Spider.XSBSpider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XSBSpiderTest {
    @Autowired
    private XSBSpider xsbSpider;

    @Test
    public void printOutTest() throws IOException {
        Collection result = xsbSpider.getInfoFromWebsite(0, 19);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(result));
    }


}
