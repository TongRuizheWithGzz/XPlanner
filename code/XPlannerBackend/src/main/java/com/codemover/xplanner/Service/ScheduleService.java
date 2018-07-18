package com.codemover.xplanner.Service;


import com.codemover.xplanner.Model.Entity.Scheduleitme;

import java.util.HashMap;

public interface ScheduleService {

    public HashMap<String, Object> findUserSchedule(Integer userId);

    public HashMap<String, Object> addScheduleItem(Scheduleitme scheduleitme);

    public HashMap<String, Object> deleteScheduleItem(Integer scheduleitemId);

    public HashMap<String, Object> modifyScheduleItem(Scheduleitme scheduleitme);

    public HashMap<String, Object> getScheduledDays(Integer userId,int year,int month);
}
