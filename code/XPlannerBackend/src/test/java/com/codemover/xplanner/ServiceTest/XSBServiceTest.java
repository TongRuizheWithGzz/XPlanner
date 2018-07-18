package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.TongquService;
import com.codemover.xplanner.Service.Impl.XSBService;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XSBServiceTest {
    @Autowired
    private XSBService xsbService;

    @Test
    public void test() throws IOException{
        xsbService.ParseHTMLFromXSB(1);
    }


}
