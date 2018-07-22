package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.XSBSpider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XSBSpiderTest {
    @Autowired
    private XSBSpider xsbSpider;

    @Test
    public void test() throws IOException{
        xsbSpider.ParseHTMLFromXSB(1);
    }


}
