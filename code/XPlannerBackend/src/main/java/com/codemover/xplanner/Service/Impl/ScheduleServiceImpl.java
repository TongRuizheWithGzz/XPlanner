package com.codemover.xplanner.Service.Impl;


import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.codemover.xplanner.Converter.ScheduleitemConverter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleItemRepository scheduleItemRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public HashMap<String, Object> findUserSchedule(String username) {

        HashMap<String, Object> response = new HashMap<>();
        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);

            throw new NullPointerException("Query user's scheduleitems: user not found");
        }

        Set<Scheduleitme> scheduleitmes = user.getScheduleitmes();
        response.put("scheduleItems", scheduleitmes);
        return response;

    }


    @Override
    public HashMap<String, Object> addScheduleItem(ScheduleitmeDTO scheduleitmeDTO, String username) {

        HashMap<String, Object> response = new HashMap<>();

        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Add a new scheduleitem: user not found");
        }

        Scheduleitme scheduleitme = ScheduleitemConverter.DTOToEntity(scheduleitmeDTO);
        scheduleitme.setUser(user);
        scheduleItemRepository.save(scheduleitme);
        return response;


    }


    @Override
    public HashMap<String, Object> deleteScheduleItem(Integer scheduleitemId, String username) {
        HashMap<String, Object> response = new HashMap<>();
        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Delete a  scheduleitem: user not found");
        }
        scheduleItemRepository.deleteById(scheduleitemId);
        return response;
    }

    @Override
    public HashMap<String, Object> updateScheduleItem(Integer scheduleitemId, ScheduleitmeDTO scheduleitmeDTO, String username) {
        HashMap<String, Object> response = new HashMap<>();
        Scheduleitme scheduleitme = scheduleItemRepository.findById(scheduleitemId).orElseThrow(
                () -> {
                    logger.warn("No such scheduleitem:'{}', will ignore", scheduleitemId);
                    return new NullPointerException("No such ");
                }
        );
        ScheduleitemConverter.modifyEntity(scheduleitme, scheduleitmeDTO);
        scheduleItemRepository.save(scheduleitme);

        return response;

    }

    public boolean IsValidTime(Scheduleitme scheduleitme) {
        Timestamp start_time = scheduleitme.getStartTime();
        Timestamp end_time = scheduleitme.getEndTime();

        //get the date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(scheduleitme.getStartTime().getTime()));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        calendar.clear();

        //get beginning
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
        calendar.set(Calendar.DATE, day);
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
        for (int i = 0; i < scheduleitmeList.size(); i++) {
            Scheduleitme tmp = scheduleitmeList.get(i);
            if (start_time.before(tmp.getEndTime()) && end_time.after(tmp.getStartTime())) return false;
        }
        return true;
    }

    @Override
    public HashMap<String, Object> findSchedule4OneDay(String username, int year, int month, int day) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setLenient(false);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
            calendar.set(Calendar.DATE, day);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp beginning = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

            calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));

            Timestamp endding = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));
            User user = userRepository.findByUserName(username);
            List<Scheduleitme> scheduleitmeList = scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(user, beginning, endding);
            response.put("errno", 0);
            response.put("errMsg", "OneDaySchedule:ok");
            response.put("scheduleitems", scheduleitmeList);
            return response;
        } catch (Exception e) {
            response.put("errno", 3);
            response.put("errMsg", "OneDaySchedule:failed");
            return response;
        }
    }

    @Override
    public HashMap<String, Object> getScheduledDays(String username, int year, int month) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setLenient(false);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String str = simpleDateFormat.format(calendar.getTime());
            Timestamp beginning = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));

            Timestamp endding = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));

            User user = userRepository.findByUserName(username);

            List<Scheduleitme> scheduleitmeList = scheduleItemRepository.findByUserAndStartTimeBetween(user, beginning, endding);

            Map<Integer, int[]> date_map = new HashMap<>();
            for (int i = 0; i < scheduleitmeList.size(); i++) {
                Timestamp timestamp_tmp = scheduleitmeList.get(i).getStartTime();
                SimpleDateFormat df_tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm");//定义格式，不显示毫秒
                String str = df_tmp.format(timestamp_tmp);
                Date date_tmp = df_tmp.parse(str);
                calendar.setTime(date_tmp);
                int day = calendar.get(Calendar.DATE);

                int[] val = date_map.get(day);
                if (val != null) {
                    val[0]++;
                } else {
                    date_map.put(day, new int[]{1});
                }
            }
            response.put("errno", 0);
            response.put("errMsg", "GetScheduledDays:ok");
            response.put("datemap", date_map);
            return response;
        } catch (DataAccessException e) {
            response.put("errno", 3);
            response.put("errMsg", "GetScheduledDays:failed");
            return response;
        } catch (ParseException e) {
            e.printStackTrace();
            response.put("errno", 3);
            response.put("errMsg", "GetScheduledDays:failed");
            return response;
        }
    }
}
