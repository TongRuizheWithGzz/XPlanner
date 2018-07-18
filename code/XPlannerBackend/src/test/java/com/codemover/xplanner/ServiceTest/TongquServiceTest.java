package com.codemover.xplanner.ServiceTest;


import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Service.Impl.TongquService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TongquServiceTest {

    @Autowired
    private TongquService tongquService;

    @Test
    public void canBuildRightUrlTest() {
        String url = tongquService.buildUrl(0, "act.create_time");
        assertThat(url).isEqualTo("https://tongqu.me/api/act/type?type=0&status=0&order=act.create_time&offset=0");
    }

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

    @Test
    public void dateEX() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 15:37:00").getTime());
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String startTtime = dFormat.format(new Date(start_time.getTime()));
        System.out.println(startTtime);
    }

}
