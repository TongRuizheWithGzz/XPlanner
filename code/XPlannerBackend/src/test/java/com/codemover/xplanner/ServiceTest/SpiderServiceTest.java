package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.SpiderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderServiceTest {

    @Autowired
    @Qualifier("DatabaseSpiderServiceImpl")
    SpiderService spiderService;


    @Test
    public void Test() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(
                mapper.writeValueAsString(
                        spiderService.getInfoFromWebsites(0, 5)));

    }
}




