package com.codemover.xplanner.Service.Impl;


import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.ScheduleService;
import com.codemover.xplanner.Service.Util.ScheduleItemDTOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleItemRepository scheduleItemRepository;

    @Override
    public HashMap<String, Object> findUserSchedule(String username) {

        HashMap<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findByUserName(username);
            Set<Scheduleitme> scheduleitmes = user.getScheduleitmes();
            Set<ScheduleitmeDTO> scheduleitmeDTOS = ScheduleItemDTOFactory.createScheduleitmeDTOsFromScheduleitmes(scheduleitmes);
            response.put("errno", 0);
            response.put("errMsg", "QueryScheduleItem:ok");
            response.put("scheduleItems", scheduleitmeDTOS);
            return response;
        } catch (DataAccessException e) {
            response.put("errno", 3);
            response.put("errMsg", "QueryScheduleItem:failed");
            return response;
        }
    }



    @Override
    public HashMap<String, Object> addScheduleItem(Scheduleitme scheduleitme) {
        HashMap<String,Object> response = new HashMap<>();
        try{
            scheduleItemRepository.save(scheduleitme);
            response.put("errno",0);
            response.put("errMsg","AddScheduleItem:ok");
            return response;
        }catch (DataAccessException e){
            response.put("errno", 3);
            response.put("errMsg", "AddScheduleItem:failed");
            return response;
        }
    }

    @Override
    public HashMap<String, Object> deleteScheduleItem(Integer scheduleitemId) {
        HashMap<String,Object> response = new HashMap<>();
        try{
            scheduleItemRepository.deleteByScheduleItmeId(scheduleitemId);
            response.put("errno",0);
            response.put("errMsg","DeleteScheduleItem:ok");
            return response;
        }catch (DataAccessException e){
            response.put("errno", 3);
            response.put("errMsg", "DeleteScheduleItem:failed");
            return response;
        }
    }

    @Override
    public HashMap<String, Object> modifyScheduleItem(Scheduleitme scheduleitme) {
        HashMap<String,Object> response = new HashMap<>();
        try{
            scheduleItemRepository.save(scheduleitme);
            response.put("errno",0);
            response.put("errMsg","ModifyScheduleItem:ok");
            return response;
        }catch (DataAccessException e){
            response.put("errno", 3);
            response.put("errMsg", "ModifyScheduleItem:failed");
            return response;
        }
    }

    @Override

    public HashMap<String, Object> getScheduledDays(String username, int year, int month) {
        HashMap<String,Object> response = new HashMap<>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
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
            response.put("errno",0);
            response.put("errMsg","GetScheduledDays:ok");
            return response;
        }catch (DataAccessException e){
            response.put("errno", 3);
            response.put("errMsg", "GetScheduledDays:failed");
            return response;
        }
    }


}
