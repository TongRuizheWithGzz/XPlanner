package com.codemover.xplanner.DaoTest.UserInheritanceTest;


import com.codemover.xplanner.DAO.*;
import com.codemover.xplanner.Model.Entity.JAccountUser;
import com.codemover.xplanner.Model.Entity.Role;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    }

    @Test
    public void aTest() throws ParseException {
        Scheduleitme scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-7-18 09:00:00").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-7-18 11:30:00").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setHas_known_concrete_time(1);
        scheduleitme.setTitle("tongruizhe");
        User user = userRepository.findByUserName("lihu");
        scheduleitme.setUserId(1);;
        System.out.println(user.getUserId());
        scheduleitme.setUser(user);

        scheduleItemRepository.save(scheduleitme);
        List<Scheduleitme> scheduleitme1 = scheduleItemRepository.findAll();
        System.out.println(scheduleitme1.get(0).getUserId());
        System.out.println(scheduleitme1.get(0).getUser().getUserId());
    }

}
