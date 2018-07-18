package com.codemover.xplanner.ServiceTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleServiceTest {

    @Test
    public void invalidDateTest(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 98, 1);
        System.out.println(calendar.getTime());
    }
}
