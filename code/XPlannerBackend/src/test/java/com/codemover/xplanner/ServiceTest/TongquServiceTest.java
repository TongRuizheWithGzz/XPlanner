package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Service.Impl.TongquService;
import com.codemover.xplanner.Service.Util.ScheduleItemDTOFactory;
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
public class TongquServiceTest {

    @Autowired
    private TongquService tongquService;


    @Test
    public void tongquOffsetParameterOffset0And1AreNotSame() {
        String response1 = tongquService.getActsFromTongqu(1, "act.create_time");
        String response2 = tongquService.getActsFromTongqu(0, "act.create_time");
        System.out.println(response1);
        assertThat(response1).isNotEqualTo(response2);
    }

    @Test
    public void tongquJsonParseSuccess() {
        HashMap<String, Object> response = tongquService.getScheduleitemsFromTongqu(0, "act.create_time");
        Gson gson = new Gson();
        String json = gson.toJson(response);
        System.out.println(json);
    }


}
