package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Converter.UploadFood.FoodPOJO;
import com.codemover.xplanner.DAO.SportItemRepository;
import com.codemover.xplanner.Model.DTO.FoodDTO;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Model.Entity.Sportsitem;
import com.codemover.xplanner.Service.IKeeperService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RandomKeeperServiceImpl implements IKeeperService {
    @Autowired
    private SportItemRepository sportItemRepository;

    public Calendar Time2TodayCalendar(Time time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s_time = simpleDateFormat.format(time);
        Calendar today_calendar = Calendar.getInstance();
        String s_today_time = simpleDateFormat1.format(today_calendar.getTime());
        s_today_time = s_today_time.substring(0, 11) + s_time;
        today_calendar.clear();
        today_calendar.setTime(simpleDateFormat1.parse(s_today_time));
        return today_calendar;
    }

    public Timestamp Calendar2Timestamp(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(calendar.getTime());
        Timestamp t_time = Timestamp.valueOf(time);
        return t_time;
    }

    @Override
    public List<KeeperRecommand> get_keeperRecommands() throws ParseException {
        List<KeeperRecommand> keeperRecommands = new LinkedList<>();
        for(int i=0;i<3;i++){
            Random random1 = new Random();
            int index = random1.nextInt(8)+1;
            Sportsitem sportsitem = sportItemRepository.findById(index).get();
            Calendar start_time = Time2TodayCalendar(sportsitem.getStartTime());
            Calendar end_time = Time2TodayCalendar(sportsitem.getEndTime());

            Timestamp t_start_time = Calendar2Timestamp(start_time);
            Timestamp t_end_time = Calendar2Timestamp(end_time);
            int relax = (int) ((t_end_time.getTime() - t_start_time.getTime()) / (60 * 1000));

            Random random = new Random();
            int ran = random.nextInt(relax-30);

            KeeperRecommand keeperRecommand = new KeeperRecommand();

            start_time.add(Calendar.MINUTE, ran);
            keeperRecommand.setStart_time(Calendar2Timestamp(start_time));

            start_time.add(Calendar.MINUTE, 30);
            keeperRecommand.setEnd_time(Calendar2Timestamp(start_time));

            keeperRecommand.setTitle(sportsitem.getSportName());

            keeperRecommand.setDescription(sportsitem.getDescription());

            keeperRecommand.setAddress(sportsitem.getAddress());

            keeperRecommand.setUser(null);

            keeperRecommands.add(keeperRecommand);
        }
        return keeperRecommands;
    }

    @Override
    public HashMap<String, Object> addUserFoodEaten(String username, LinkedList<FoodPOJO> foodPOJOS) {
        return null;
    }

    @Override
    public void setState(String username, Integer sportsItemNumber, Integer caloriee) {

    }

    @Override
    public List<FoodDTO> getFoodList(String dininghall) {
        return null;
    }
}
