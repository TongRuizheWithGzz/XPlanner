package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class KeeperTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Before
    public void setup() throws ParseException {
        Scheduleitme scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-22 21:00").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-22 22:00").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user = userRepository.findByUserName("lihu");
        scheduleitme.setUser(user);
        scheduleitme.setHasKnownConcreteTime(true);
        scheduleItemRepository.save(scheduleitme);
        Scheduleitme scheduleitme1 = new Scheduleitme();
        java.sql.Timestamp start_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-22 22:00").getTime());
        scheduleitme1.setStartTime(start_time1);
        java.sql.Timestamp end_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-22 22:30").getTime());
        scheduleitme1.setEndTime(end_time1);
        scheduleitme1.setDescription("软件工程");
        scheduleitme1.setAddress("软件大楼");
        scheduleitme1.setTitle("tongruizhe");
        scheduleitme1.setUser(user);
        scheduleitme1.setHasKnownConcreteTime(true);
        scheduleItemRepository.save(scheduleitme1);
    }

    @Test
    public void aTest() throws ParseException {
        List<Scheduleitme> scheduleitmes = scheduleItemRepository.findAll();
        List<HashMap<String,Calendar>> list = get_busy_time_pool(scheduleitmes);

    }

    public Calendar timestamp2calendar(Timestamp timestamp) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(timestamp.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(time));
        return calendar;
    }

    private class ScheduletimeComperator implements Comparator<Scheduleitme> {
        @Override
        public int compare(Scheduleitme o1, Scheduleitme o2) {
            return o1.getStartTime().before(o2.getStartTime()) ? -1 :o1.getStartTime().equals(o2.getStartTime()) ? 0 : 1;
        }
    }

    public List<HashMap<String,Calendar>> get_busy_time_pool(List<Scheduleitme> scheduleitmes) throws ParseException {
        Collections.sort(scheduleitmes,new ScheduletimeComperator());
        LinkedList<HashMap<String,Calendar>> busy_time_pool = new LinkedList<>();
        for(Scheduleitme scheduleitme:scheduleitmes){
            if (busy_time_pool.isEmpty() || busy_time_pool.getLast().get("end_time").before(timestamp2calendar(scheduleitme.getStartTime()))) {
                HashMap<String,Calendar> node = new HashMap<>();
                node.put("start_time",timestamp2calendar(scheduleitme.getStartTime()));
                node.put("end_time",timestamp2calendar(scheduleitme.getEndTime()));
                busy_time_pool.add(node);
            }
            else{
                Calendar new_end = busy_time_pool.getLast().get("end_time");
                if(busy_time_pool.getLast().get("end_time").before(timestamp2calendar(scheduleitme.getEndTime()))){
                    new_end = timestamp2calendar(scheduleitme.getEndTime());
                }
                busy_time_pool.getLast().put("end_time",new_end);
            }
        }
        return busy_time_pool;

    }

}
