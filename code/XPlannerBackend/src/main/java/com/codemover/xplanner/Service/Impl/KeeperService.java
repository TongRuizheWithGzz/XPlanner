package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.DAO.KeeperRecommandRepository;
import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.SportItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.Sportsitem;
import com.codemover.xplanner.Model.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class KeeperService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private SportItemRepository sportItemRepository;

    @Autowired
    private KeeperRecommandRepository keeperRecommandRepository;

   /* <---------------------------Tools-------------------------->*/
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

    /* <---------------------------Variables-------------------------->*/

    //todays'scheduleitems
    private List<Scheduleitme> scheduleitmes;

    //a list of busy time
    private LinkedList<HashMap<String,Calendar>> busy_time_pool = new LinkedList<>();

    //username and user(while setting username,user is automatically set)
    private String username;
    private User user;

    //you can set sports_number to decide how many recommend-items to be recommended to user
    private int sports_number;

    //calorie is set after it is calculated
    private int calorie;

    //a list of relax time determined by busy_time_pool,now_time and period that you can do a specific sport
    private LinkedList<HashMap<String,Calendar>> relax_time_pool = new LinkedList<>();

    //a specific sportsitem
    private Sportsitem sportsitem;

    //minutes is determined by sportsitem and calorie
    private int minutes;

    /* <---------------------------Set Variables-------------------------->*/
    public void setSports_number(int sports_number) {
        this.sports_number = sports_number;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public void setUsername(String username) {
        this.username = username;
        this.user = userRepository.findByUserName(username);
    }

    /* <---------------------------executable-------------------------->*/

    //it is used in get_user_busy_time_pool() to calculate for busy_time_pool
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

    //get user's today's scheduleitems and then calculate for busy_time_pool
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

    //it is used in random_n_relax_by_sports() to calculate for relax_time_pool
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

    //it is used in random_n_relax_by_sports() to get a item's start_time and end_time by random
    private void randomFreeTime(HashMap<String,Calendar> time_node) throws ParseException {
        Calendar start_time = Calendar.getInstance();
        start_time.setTime(time_node.get("start_time").getTime());
        Calendar end_time = Calendar.getInstance();
        end_time.setTime(time_node.get("end_time").getTime());

        Timestamp t_start_time = Calendar2Timestamp(start_time);
        Timestamp t_end_time = Calendar2Timestamp(end_time);
        int relax = (int) ((t_end_time.getTime()-t_start_time.getTime())/(60*1000));

        Random random = new Random();
        int ran = random.nextInt(relax-minutes);

        KeeperRecommand keeperRecommand = new KeeperRecommand();

        start_time.add(Calendar.MINUTE,ran);
        keeperRecommand.setStart_time(Calendar2Timestamp(start_time));

        start_time.add(Calendar.MINUTE,minutes);
        keeperRecommand.setEnd_time(Calendar2Timestamp(start_time));

        keeperRecommand.setTitle(sportsitem.getSportName());

        keeperRecommand.setDescription(sportsitem.getDescription());

        keeperRecommand.setAddress(sportsitem.getAddress());

        keeperRecommand.setUser(user);
        keeperRecommandRepository.save(keeperRecommand);
    }

    //it is used in get_keeperRecommands() whether to refresh recomment-items
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
        if(!s_now.substring(0,10).equals(s_fresh.substring(0,10))){
            return true;
        }
        return false;
    }

    //it is used in get_keeperRecommands() to get recomment-items
    public void random_n_relax_by_sports() throws ParseException {
        int sportsItem_number = (int) sportItemRepository.count();
        Random random = new Random();
        Calendar now = Calendar.getInstance();

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
            int relax_time_pool_size = relax_time_pool.size();
            if(relax_time_pool_size==0){
                System.out.println("no relax time!");
                continue;
            }
            int index = random1.nextInt(relax_time_pool_size);

            randomFreeTime(relax_time_pool.get(index));
        }
    }

    //whether to refresh recommend-items and refresh recommend-items if needed
    //String username,int sports_number
    public List<KeeperRecommand> get_keeperRecommands() throws ParseException {
        if(need_to_fresh()){
            keeperRecommandRepository.deleteByUser(user);

            //set calorie
            get_user_busy_time_pool();
            random_n_relax_by_sports();

            //refresh user
            Calendar now = Calendar.getInstance();
            user.setLast_keeper_fresh(Calendar2Timestamp(now));
            userRepository.save(user);
        }
        return keeperRecommandRepository.findByUser(user);
    }

}
