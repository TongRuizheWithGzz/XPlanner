package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.SpiderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderServiceTest {

    @Autowired
    SpiderService spiderService;


    @Test
    public void Test() throws Exception {
        for (int i = 0; i < 10; i++) {
            spiderService.getInfoFromWebsites(5, 2);
        }

    }
}




