package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.DAO.KeeperRecommandRepository;
import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.SportItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.Sportsitem;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.Impl.KeeperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class KeeperServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private SportItemRepository sportItemRepository;

    @Autowired
    private KeeperRecommandRepository keeperRecommandRepository;

    public Calendar timestamp2calendar(Timestamp timestamp) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(timestamp.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(time));
        return calendar;
    }

    public Calendar Time2TodayCalendar(Time time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s_time = simpleDateFormat.format(time);
        Calendar today_calendar = Calendar.getInstance();
        String s_today_time = simpleDateFormat1.format(today_calendar.getTime());
        s_today_time = s_today_time.substring(0,11) + s_time;
        today_calendar.clear();
        today_calendar.setTime(simpleDateFormat1.parse(s_today_time));
        return today_calendar;
    }

    public Timestamp Calendar2Timestamp(Calendar calendar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(calendar.getTime());
        Timestamp t_time = Timestamp.valueOf(time);
        return t_time;
    }

    public String Calendar2String(Calendar calendar) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());
        return time;
    }

    private class ScheduletimeComperator implements Comparator<Scheduleitme> {
        @Override
        public int compare(Scheduleitme o1, Scheduleitme o2) {
            return o1.getStartTime().before(o2.getStartTime()) ? -1 :o1.getStartTime().equals(o2.getStartTime()) ? 0 : 1;
        }
    }

    private LinkedList<HashMap<String,Calendar>> busy_time_pool = new LinkedList<>();

    private List<Scheduleitme> scheduleitmes;

    public void get_busy_time_pool() throws ParseException {
        Collections.sort(scheduleitmes,new ScheduletimeComperator());
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
    }

    private String username;

    private User user;

    public void setUsername(String username) {
        this.username = username;
        this.user = userRepository.findByUserName(username);
    }

    public void get_user_busy_time_pool() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        //get begin and end of today
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        Timestamp begin_of_day = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        Timestamp end_of_day = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

        //get today's scheduleitems
        scheduleitmes = scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(user,begin_of_day, end_of_day);

        get_busy_time_pool();
    }

    private int sports_number;

    public int getSports_number() {
        return sports_number;
    }

    public void setSports_number(int sports_number) {
        this.sports_number = sports_number;
    }

    private int calorie;

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    private LinkedList<HashMap<String,Calendar>> relax_time_pool = new LinkedList<>();

    private Sportsitem sportsitem;

    private int minutes;

    public void push_time_pool(Calendar start_time,Calendar end_time){
        Timestamp t_start_time = Calendar2Timestamp(start_time);
        Timestamp t_end_time = Calendar2Timestamp(end_time);
        int relax = (int) ((t_end_time.getTime()-t_start_time.getTime())/(60*1000));
        if(relax>15 && relax>minutes){
            Calendar s_time = Calendar.getInstance();
            s_time.setTime(start_time.getTime());
            Calendar e_time = Calendar.getInstance();
            e_time.setTime(end_time.getTime());
            HashMap<String,Calendar> relax_time = new HashMap<>();
            System.out.println("push:");
            System.out.println(s_time.getTime());
            System.out.println(e_time.getTime());
            relax_time.put("start_time",s_time);
            relax_time.put("end_time",e_time);
            relax_time_pool.add(relax_time);
        }
    }

    public void random_n_relax_by_sports() throws ParseException {
        int sportsItem_number = (int) sportItemRepository.count();
        Random random = new Random();
        Calendar now = Calendar.getInstance();

        //fate time
/*        now.set(Calendar.HOUR_OF_DAY,15);
        now.set(Calendar.MINUTE,0);*/

        for(int i = 0 ; i < sports_number ; i++){
            int sportItem_id = random.nextInt(sportsItem_number) + 1;
            sportsitem = sportItemRepository.findById(sportItem_id).get();

            int calorie_per_minute = sportsitem.getCaloriePerMinute();
            minutes = calorie/calorie_per_minute;

            //get start_time and end_time for a sports'relax time
            Calendar start_time = Time2TodayCalendar(sportsitem.getStartTime());
            Calendar end_time = Time2TodayCalendar(sportsitem.getEndTime());
            System.out.println(start_time.getTime());
            System.out.println(end_time.getTime());
            if(now.after(end_time))continue;
            else if(now.after(start_time)) start_time.setTime(now.getTime());

            //find_relax_time
            relax_time_pool.clear();
            for(HashMap<String,Calendar> node:busy_time_pool){
                if(start_time.after(node.get("end_time")))continue;
                if(end_time.before(node.get("start_time")))break;
                if(end_time.before(node.get("end_time"))&&start_time.after(node.get("start_time")))break;

                if(start_time.after(node.get("start_time"))){
                    start_time.setTime(node.get("end_time").getTime());
                    break;
                }
            }

            //whether need push after for
            int time_flag = 0;
            for(HashMap<String,Calendar> node:busy_time_pool){
                if(start_time.after(node.get("end_time"))) continue;
                if(end_time.before(node.get("start_time")))break;
                if(end_time.before(node.get("end_time"))&&start_time.after(node.get("start_time"))){
                    time_flag = 1;
                }

                if(end_time.before(node.get("start_time"))){
                    push_time_pool(start_time,end_time);
                    time_flag = 1;
                    break;
                }
                else{
                    push_time_pool(start_time,node.get("start_time"));
                    start_time.setTime(node.get("end_time").getTime());
                }

            }
            if(time_flag == 0) push_time_pool(start_time,end_time);

            Random random1 = new Random();
            int index = random1.nextInt(relax_time_pool.size());

            randomFreeTime(relax_time_pool.get(index));
        }
    }

    private ArrayList<HashMap<String,String>> found_time_sports = new ArrayList<>();

    public List<HashMap<String,String>> getFound_time_sports() {
        return found_time_sports;
    }

    private void randomFreeTime(HashMap<String,Calendar> time_node) throws ParseException {
        Calendar start_time = Calendar.getInstance();
        start_time.setTime(time_node.get("start_time").getTime());
        Calendar end_time = Calendar.getInstance();
        end_time.setTime(time_node.get("end_time").getTime());

        Timestamp t_start_time = Calendar2Timestamp(start_time);
        Timestamp t_end_time = Calendar2Timestamp(end_time);
        int relax = (int) ((t_end_time.getTime()-t_start_time.getTime())/(60*1000));

        HashMap<String ,String> found_time_sport = new HashMap<>();
        Random random = new Random();
        int ran = random.nextInt(relax-minutes);

        KeeperRecommand keeperRecommand = new KeeperRecommand();

        start_time.add(Calendar.MINUTE,ran);
        found_time_sport.put("start_time",Calendar2String(start_time));
        keeperRecommand.setStart_time(Calendar2Timestamp(start_time));

        start_time.add(Calendar.MINUTE,minutes);
        found_time_sport.put("end_time",Calendar2String(start_time));
        keeperRecommand.setEnd_time(Calendar2Timestamp(start_time));

        found_time_sport.put("title",sportsitem.getSportName());
        keeperRecommand.setTitle(sportsitem.getSportName());

        found_time_sport.put("description",sportsitem.getDescription());
        keeperRecommand.setDescription(sportsitem.getDescription());

        found_time_sport.put("address",sportsitem.getAddress());
        keeperRecommand.setAddress(sportsitem.getAddress());

        System.out.println(found_time_sport.get("start_time"));
        System.out.println(found_time_sport.get("end_time"));
        found_time_sports.add(found_time_sport);

        keeperRecommand.setUser(user);
        keeperRecommandRepository.save(keeperRecommand);
    }

    public boolean need_to_fresh(){
        Timestamp last_keeper_fresh = user.getLast_keeper_fresh();
        if(last_keeper_fresh==null){
            return true;
        }
        Calendar now = Calendar.getInstance();
        int interval = (int) ((Calendar2Timestamp(now).getTime()-last_keeper_fresh.getTime())/(60*1000));
        if(interval>10) return true;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s_now = simpleDateFormat.format(now.getTime());
        String s_fresh = simpleDateFormat.format(last_keeper_fresh.getTime());
        if(!s_now.substring(0,10).equals(s_fresh.substring(0,10)))return true;
        return false;
    }

    public List<KeeperRecommand> get_keeperRecommands() throws ParseException {

        if(need_to_fresh()){
            keeperRecommandRepository.deleteByUser(user);
            get_user_busy_time_pool();
            random_n_relax_by_sports();
            Calendar now = Calendar.getInstance();
            user.setLast_keeper_fresh(Calendar2Timestamp(now));
        }
        return keeperRecommandRepository.findByUser(user);
    }

    @Before
    public void setup() throws ParseException {
        Scheduleitme scheduleitme = new Scheduleitme();
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
        scheduleItemRepository.save(scheduleitme);

        Scheduleitme scheduleitme1 = new Scheduleitme();
        java.sql.Timestamp start_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-23 14:00").getTime());
        scheduleitme1.setStartTime(start_time1);
        java.sql.Timestamp end_time1 = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-23 15:00").getTime());
        scheduleitme1.setEndTime(end_time1);
        scheduleitme1.setDescription("软件工程");
        scheduleitme1.setAddress("软件大楼");
        scheduleitme1.setTitle("tongruizhe");
        scheduleitme1.setUser(user0);
        scheduleItemRepository.save(scheduleitme1);

        KeeperRecommand keeperRecommand = new KeeperRecommand();
        keeperRecommand.setStart_time(start_time1);
        keeperRecommand.setEnd_time(end_time1);
        keeperRecommand.setTitle("tongruizhe");
        keeperRecommand.setDescription("电子信息");
        keeperRecommand.setAddress("电院");
        keeperRecommand.setUser(user0);

        keeperRecommandRepository.save(keeperRecommand);

        User user1 = userRepository.findByUserName("tongruizhe");
        KeeperRecommand keeperRecommand1 = new KeeperRecommand();
        keeperRecommand1.setStart_time(start_time1);
        keeperRecommand1.setEnd_time(end_time1);
        keeperRecommand1.setTitle("tongruizhee");
        keeperRecommand1.setDescription("电子信息");
        keeperRecommand1.setAddress("电院");
        keeperRecommand1.setUser(user1);

        keeperRecommandRepository.save(keeperRecommand1);
    }

    @Test
    public void aTest() throws ParseException {
        //set up
        setUsername("lihu");
        get_user_busy_time_pool();
        setSports_number(3);
        setCalorie(332);

        //run
        random_n_relax_by_sports();

        //get like this
        System.out.println(getFound_time_sports().size());
    }

    @Test
    public void bTest() throws ParseException {
        //setup
        setUsername("lihu");

        //run
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp t = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-13 10:16").getTime());
        user.setLast_keeper_fresh(t);
        System.out.println(need_to_fresh());

    }

    @Test
    public void timeconvert() throws ParseException {
        String s = "2018-7-23 12:23:31";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        Timestamp t = new Timestamp(date.getTime());
        System.out.println(t);
    }

    @Test
    public void cTest() throws ParseException {
        setUsername("lihu");
        setSports_number(3);
        setCalorie(332);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.sql.Timestamp t = new java.sql.Timestamp(simpleDateFormat.parse("2018-07-23 11:01").getTime());
        user.setLast_keeper_fresh(t);

        List<KeeperRecommand> list = get_keeperRecommands() ;
        for (KeeperRecommand keeperRecommand:list) {
            System.out.println("now recommands");
            System.out.println(keeperRecommand.getTitle());
            System.out.println(keeperRecommand.getDescription());
            System.out.println(keeperRecommand.getAddress());
            System.out.println(keeperRecommand.getStart_time());
            System.out.println(keeperRecommand.getEnd_time());
        }
    }
}
