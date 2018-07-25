package com.codemover.xplanner.Service.Impl;


import com.codemover.xplanner.Converter.ScheduleitemConverter;
import com.codemover.xplanner.Converter.UpdateScheduleitmeRequest;
import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        LinkedList<ScheduleitmeDTO> scheduleitmeDTOS = ScheduleitemConverter.entitiesToDTOs(scheduleitmes);
        response.put("scheduleItems", scheduleitmeDTOS);
        return response;

    }


    @Override
    public HashMap<String, Object>
    addScheduleItem(UpdateScheduleitmeRequest request, String username) {
        logger.debug("Get scheduleitem title:'{}'", request.title);
        HashMap<String, Object> response = new HashMap<>();

        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Add a new scheduleitem: user not found");
        }

        Scheduleitme newScheduleitem = ScheduleitemConverter.DTOToEntity(request.toScheduleitemDTO());
        newScheduleitem.setUser(user);
        Scheduleitme s = scheduleItemRepository.save(newScheduleitem);

        response.put("newScheduleitem", ScheduleitemConverter.entityToDTO(newScheduleitem));

        response.put("newScheduleitmesForDay", ScheduleitemConverter
                .entitiesToDTOs(getScheduleitemsForDay(user, request.year, request.month, request.day)));
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
    public HashMap<String, Object> updateScheduleItem(Integer scheduleitemId, UpdateScheduleitmeRequest request, String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Delete a  scheduleitem: user not found");
        }

        HashMap<String, Object> response = new HashMap<>();
        Scheduleitme scheduleitme = scheduleItemRepository.findById(scheduleitemId).orElseThrow(
                () -> {
                    logger.warn("No such scheduleitem:'{}', will ignore", scheduleitemId);
                    return new NullPointerException("No such ");
                }
        );
        ScheduleitemConverter.modifyEntity(scheduleitme, request.toScheduleitemDTO());
        scheduleItemRepository.save(scheduleitme);
        response.put("scheduleItems", ScheduleitemConverter
                .entitiesToDTOs(getScheduleitemsForDay(user, request.year, request.month, request.day)));
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

        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Delete a  scheduleitem: user not found");
        }

//        Calendar calendar = Calendar.getInstance();
//        calendar.clear();
//        calendar.setLenient(false);
//        calendar.set(Calendar.YEAR, year);
//        calendar.set(Calendar.MONTH, month - 1);//注意,Calendar对象默认一月为0
//        calendar.set(Calendar.DATE, day);
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Timestamp beginning = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));
//
//
//        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
//        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
//        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
//
//
//        Timestamp endding = Timestamp.valueOf(simpleDateFormat.format(calendar.getTime()));
//
//        Collection<Scheduleitme> scheduleitmeList =
//                scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(user, beginning, endding);

        response.put("scheduleitems", ScheduleitemConverter
                .entitiesToDTOs(getScheduleitemsForDay(user, year, month, day)));
        return response;

    }

    private Collection<Scheduleitme> getScheduleitemsForDay(User user, int year, int month, int day) {
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

        Collection<Scheduleitme> scheduleitmeList =
                scheduleItemRepository.findByUserAndStartTimeBetweenOrderByStartTimeAsc(user, beginning, endding);
        ObjectMapper mapper = new ObjectMapper();

        return scheduleitmeList;

    }

    @Override
    public HashMap<String, Object> getScheduledDays(String username, int year, int month)
            throws ParseException {
        HashMap<String, Object> response = new HashMap<>();
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
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Delete a  scheduleitem: user not found");
        }
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
        response.put("dateMap", date_map);
        return response;
    }

    @Override
    public HashMap<String, Object> changeCompleteState(String username, Integer id, boolean completed) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Change the state of  a  scheduleitem: user not found");
        }
        Scheduleitme scheduleitme = scheduleItemRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("No such scheduleitem:'{}', will ignore", id);
                    return new NullPointerException("No such ");
                }
        );
        if (scheduleitme.isCompleted() == completed) {
            logger.warn("Unused modification for schedueitem:'{}'", id);
        }

        HashMap<String, Object> response = new HashMap<>();
        scheduleitme.setCompleted(completed);
        scheduleItemRepository.save(scheduleitme);
        return response;
    }
}
