package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Service.Impl.KeeperService;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeeperDataTest {
    @Autowired
    private KeeperService keeperService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Before
    public void setup(){
        DataFactory df = new DataFactory();
        String[] usernames = {"tongruizhe","lihu"};
        String[] title = {"item1","item2","item3","item4","item5","item6","item7"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        int minutes = 24*60;
        int[] last_minutes = {15,30,45,60,75,90,120};

        for(int i=0;i<30;i++){
            Scheduleitme scheduleitme = new Scheduleitme();
            scheduleitme.setTitle(df.getItem(title));
            scheduleitme.setDescription("测试数据");
            scheduleitme.setAddress(df.getAddress());
            scheduleitme.setCompleted(false);
            scheduleitme.setUser(userRepository.findByUserName(df.getItem(usernames)));
            Random random = new Random();
            int ran = random.nextInt(minutes)+1;
            int last = last_minutes[random.nextInt(last_minutes.length)];
            Calendar start_time = Calendar.getInstance();
            start_time.setTime(calendar.getTime());
            start_time.add(Calendar.MINUTE,ran);
            System.out.println("start_time");
            System.out.println(start_time.getTime());
            scheduleitme.setStartTime(keeperService.Calendar2Timestamp(start_time));
            start_time.add(Calendar.MINUTE,last);
            System.out.println("end_time");
            System.out.println(start_time.getTime());
            scheduleitme.setEndTime(keeperService.Calendar2Timestamp(start_time));
            scheduleItemRepository.save(scheduleitme);
        }
    }

    @Test
    public void test() throws ParseException {
        /*keeperService.setUsername("lihu");
        keeperService.setCalorie(200);
        keeperService.setSports_number(3);

        System.out.println(keeperService.get_keeperRecommands().size());*/
    }
}
