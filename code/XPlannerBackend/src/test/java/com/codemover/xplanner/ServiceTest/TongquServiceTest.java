package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.TongquService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TongquServiceTest {

    @Autowired
    private TongquService tongquService;

    @Test
    public void canBuildRightUrlTest() {
        String url = tongquService.buildUrl(0, "act.create_time");
        assertThat(url).isEqualTo("https://tongqu.me/api/act/type?" +
                "type=0&status=0&order=act.create_time&offset=0");
    }

}
