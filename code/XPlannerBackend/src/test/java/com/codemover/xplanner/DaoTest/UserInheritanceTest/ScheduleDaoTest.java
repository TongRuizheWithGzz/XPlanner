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
        java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 05:00").getTime());
        scheduleitme.setStartTime(start_time);
        java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 06:00").getTime());
        scheduleitme.setEndTime(end_time);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user = userRepository.findByUserName("lihu");
        scheduleitme.setUser(user);
        scheduleitme.setHasKnownConcreteTime(true);
        scheduleItemRepository.save(scheduleitme);
        Scheduleitme scheduleitme1 = new Scheduleitme();
        java.sql.Timestamp start_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 06:00").getTime());
        scheduleitme1.setStartTime(start_time1);
        java.sql.Timestamp end_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-18 10:30").getTime());
        scheduleitme1.setEndTime(end_time1);
        scheduleitme1.setDescription("软件工程");
        scheduleitme1.setAddress("软件大楼");
        scheduleitme1.setTitle("tongruizhe");
        scheduleitme1.setUser(user);
        scheduleitme1.setHasKnownConcreteTime(true);
        scheduleItemRepository.save(scheduleitme1);
    }

    @Test
    public void TimeStamp_Valid_Test() throws ParseException {
        Scheduleitme scheduleitme = new Scheduleitme();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp start_time1 = new java.sql.Timestamp(simpleDateFormat1.parse("2018-07-18 05:30").getTime());
        scheduleitme.setStartTime(start_time1);
        java.sql.Timestamp end_time1 = new java.sql.Timestamp(simpleDateFormat1.parse("2018-07-18 09:40").getTime());
        scheduleitme.setEndTime(end_time1);
        scheduleitme.setDescription("软件工程");
        scheduleitme.setAddress("软件大楼");
        scheduleitme.setTitle("tongruizhe");
        User user1 = userRepository.findByUserName("lihu");
        scheduleitme.setUser(user1);
        scheduleitme.setHasKnownConcreteTime(true);

        Timestamp start_time = scheduleitme.getStartTime();
        Timestamp end_time = scheduleitme.getEndTime();

        //get the date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(scheduleitme.getStartTime().getTime()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        calendar.clear();

        //get beginning
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        calendar.set(Calendar.DATE,day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp beginning = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

        //get endding
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        Timestamp endding = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

        //get scheduleList
        User user = scheduleitme.getUser();
        List<Scheduleitme> scheduleitmeList = scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(user, beginning, endding);

        //compare timestamp
        for(int i=0;i<scheduleitmeList.size();i++){
            Scheduleitme tmp = scheduleitmeList.get(i);
            if(start_time.before(tmp.getEndTime()) && end_time.after(tmp.getStartTime()))System.out.println("false");
        }
        System.out.println("true");
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
    public void dateTest() throws ParseException {
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

        Map<Integer,int[]> date_map = new HashMap<>();
        for(int i = 0;i<scheduleitmeList.size();i++){
            Timestamp timestamp_tmp = scheduleitmeList.get(i).getStartTime();
            SimpleDateFormat df_tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
            String str = df_tmp.format(timestamp_tmp);
            Date date_tmp = df_tmp.parse(str);
            calendar.setTime(date_tmp);
            int day = calendar.get(Calendar.DATE);

            int[] val = date_map.get(day);
            if(val!=null){
                val[0]++;
            }
            else {
                date_map.put(day,new int[]{1});
            }
        }
        System.out.println(date_map.get(18)[0]);
    }

    @Test
    public void date_validString(){
        try{
        String str = "2018-2-29 12:00";
        SimpleDateFormat df_tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        df_tmp.setLenient(false);
        Date date = df_tmp.parse(str);}
        catch (ParseException e){
            System.out.println("Invalid time");
        }
    }

    @Test
    public void date_validYearMonthDay(){
        int year = 2018;
        int month = 12;
        int day = 30;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setLenient(false);

        try{
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0
            calendar.set(Calendar.DATE,day);
            System.out.println(calendar.getTime());
        }catch (Exception e){
            System.out.println("Invalid time");
        }
    }

    @Test
    public void OneDaySchedule(){
        int year = 2018;
        int month = 7;
        int day = 18;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setLenient(false);
        try{
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
            calendar.set(Calendar.DATE,day);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String str = simpleDateFormat.format(calendar.getTime());
            Timestamp beginning = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));

            Timestamp endding = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));
            User user = userRepository.findByUserName("lihu");
            List<Scheduleitme> scheduleitmeList = scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(user, beginning, endding);
            Iterator<Scheduleitme> it = scheduleitmeList.iterator();
            while(it.hasNext()){
                System.out.println(it.next().getStartTime());
            }
        }catch (Exception e){
            System.out.println("Invalid Time");
        }

    }

}
