package com.codemover.xplanner.Service.Impl;


import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.ScheduleService;
import com.codemover.xplanner.Service.Util.ScheduleItemDTOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    UserRepository userRepository;

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
    public HashMap<String, Object> findScheduleitemByDay(Integer year, Integer month, Integer day, String username) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return null;
    }

    @Override
    public HashMap<String, Object> addScheduleItem(Scheduleitme scheduleitme) {
        return null;
    }

    @Override
    public HashMap<String, Object> deleteScheduleItem(Integer scheduleitemId) {
        return null;
    }

    @Override
    public HashMap<String, Object> modifyScheduleItem(Scheduleitme scheduleitme) {
        return null;
    }

    @Override
    public HashMap<String, Object> queryDateHavingScheduleitemByYearAndMonth(String username, Integer year, Integer month) {
        return null;
    }

}
