package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.DAO.KeeperRecommandRepository;
import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.Impl.KeeperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KeeperTest {
    @Autowired
    private KeeperService keeperService;

    @MockBean
    private ScheduleItemRepository scheduleItemRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KeeperRecommandRepository keeperRecommandRepository;

    private User user;

    private Scheduleitme scheduleitme;

    private Scheduleitme scheduleitme1;

    private List<Scheduleitme> scheduleitmeList;

    private KeeperRecommand keeperRecommand;

    private List<KeeperRecommand> keeperRecommandList;

    @Before
    public void setup() throws ParseException {
        user=new User();
        user.setUserName("lihu");

        scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-23 21:10").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-23 21:14").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user0 = userRepository.findByUserName("lihu");
        scheduleitme.setUser(user0);

        scheduleitme1 = new Scheduleitme();
        java.sql.Timestamp start_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-27 14:00").getTime());
        scheduleitme1.setStartTime(start_time1);
        java.sql.Timestamp end_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-27 15:00").getTime());
        scheduleitme1.setEndTime(end_time1);
        scheduleitme1.setDescription("软件工程");
        scheduleitme1.setAddress("软件大楼");
        scheduleitme1.setTitle("tongruizhe");
        scheduleitme1.setUser(user0);

        scheduleitmeList = new ArrayList<>();
        scheduleitmeList.add(scheduleitme);
        scheduleitmeList.add(scheduleitme1);

        keeperRecommand = new KeeperRecommand();
        keeperRecommand.setStart_time(start_time1);
        keeperRecommand.setEnd_time(end_time1);
        keeperRecommand.setTitle("tongruizhe");
        keeperRecommand.setDescription("电子信息");
        keeperRecommand.setAddress("电院");
        keeperRecommand.setUser(user0);

        keeperRecommandList = new ArrayList<>();
    }
    @Test()
    public void test() throws ParseException {
        when(userRepository.findByUserName(any())).thenReturn(user);
        when(scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(any(),any(),any())).thenReturn(scheduleitmeList);
        when(keeperRecommandRepository.findByUser(any())).thenReturn(keeperRecommandList);
        keeperService.setUsername("lihu");
        keeperService.setSports_number(3);
        keeperService.setCalorie(332);
        keeperService.get_keeperRecommands();
        System.out.println(keeperService.get_keeperRecommands().size());
    }


}
