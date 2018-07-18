package com.codemover.xplanner.DaoTest.UserInheritanceTest;


import com.codemover.xplanner.DAO.*;
import com.codemover.xplanner.Model.Entity.JAccountUser;
import com.codemover.xplanner.Model.Entity.Role;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.ScheduleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScheduleDaoTest {

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private JAccountUserRepository jAccountUserRepository;

    @Autowired
    private WeixinUserRepository weixinUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void setup() throws ParseException {
        Scheduleitme scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 09:00").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 11:30").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user = userRepository.findByUserName("lihu");
        scheduleitme.setUser(user);
        scheduleitme.setHasKnownConcreteTime(true);
        scheduleItemRepository.save(scheduleitme);
    }

    @Test
    public void canGetUserScheduleitemTest() throws ParseException {
        Scheduleitme scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 09:00:00").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 11:30:00").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user = userRepository.findByUserName("lihu");
        System.out.println(user.getUserId());
        scheduleitme.setUser(user);
        scheduleitme.setHasKnownConcreteTime(true);
        scheduleItemRepository.save(scheduleitme);
        List<Scheduleitme> scheduleitme1 = scheduleItemRepository.findAll();
    }

    @Test
    public void canModifyUserScheduleitemTest() throws ParseException {
        List<Scheduleitme> scheduleitme1 = scheduleItemRepository.findAll();

        Scheduleitme scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 09:00:00").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 11:30:00").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user = userRepository.findByUserName("lihu");
        scheduleitme.setUser(user);
        scheduleitme.setHasKnownConcreteTime(true);
        scheduleitme.setScheduleItmeId(scheduleitme1.get(0).getScheduleItmeId());
        System.out.println(scheduleitme.getScheduleItmeId());
        scheduleItemRepository.save(scheduleitme);
        List<Scheduleitme> scheduleitme2 = scheduleItemRepository.findAll();
        System.out.println(scheduleitme2.size());
    }

    @Test
    public void dateTest(){
        int year = 2018;
        int month = 7;

        Calendar calendar=Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String str = simpleDateFormat.format(calendar.getTime());
        Timestamp beginning = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));

        Timestamp endding = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

        List<Scheduleitme> list = scheduleItemRepository.findAll();

        User user = userRepository.findByUserName("lihu");
        List<Scheduleitme> scheduleitmeList = scheduleItemRepository.findByUserAndStartTimeBetween(user,beginning,endding);
        System.out.println(scheduleitmeList.size());
    }
}
