package com.codemover.xplanner.Service.Impl;


import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleItemRepository scheduleItemRepository;

    @Override
    public HashMap<String, Object> findUserSchedule(Integer userId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            List<Scheduleitme> scheduleitmes = scheduleItemRepository.findByUserId(userId);
            response.put("errno", 0);
            response.put("errMsg", "QueryScheduleItem:ok");
            response.put("scheduleItems", scheduleitmes);
            return response;
        } catch (DataAccessException e) {
            response.put("errno", 3);
            response.put("errMsg", "QueryScheduleItem:failed");
            return response;
        }
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

}
