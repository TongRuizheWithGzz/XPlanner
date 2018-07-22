package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.TongquXSpider;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TongquXSpiderTest {

    @Autowired
    private TongquXSpider tongquXSpider;


    @Test
    public void tongquOffsetParameterOffset0And1AreNotSame() {
        String response1 = tongquXSpider.getActsFromTongqu(1, "act.create_time");
        String response2 = tongquXSpider.getActsFromTongqu(0, "act.create_time");
        System.out.println(response1);
        assertThat(response1).isNotEqualTo(response2);
    }

    @Test
    public void tongquJsonParseSuccess() {
        HashMap<String, Object> response = tongquXSpider.getScheduleitemsFromTongqu(0, "act.create_time");
        Gson gson = new Gson();
        String json = gson.toJson(response);
        System.out.println(json);
    }


}
