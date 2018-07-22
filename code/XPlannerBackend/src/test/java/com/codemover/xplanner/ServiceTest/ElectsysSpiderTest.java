package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.ElectsysSpider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElectsysSpiderTest {

    @Autowired
    ElectsysSpider electsysSpider;

    @Test
    public void test() throws IOException {
        electsysSpider.getInfoFromElectsys();

    }
}


