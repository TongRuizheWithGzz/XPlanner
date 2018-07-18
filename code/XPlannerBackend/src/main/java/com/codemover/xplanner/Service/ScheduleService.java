package com.codemover.xplanner.Service;


import com.codemover.xplanner.Model.Entity.Scheduleitme;

import java.util.HashMap;

public interface ScheduleService {

    public HashMap<String, Object> findUserSchedule(String username);

    public HashMap<String,Object>  findScheduleitemByDay(Integer year,Integer month,Integer day,String username);

    public HashMap<String,Object> queryDateHavingScheduleitemByYearAndMonth(String username,Integer year,Integer month);


    public HashMap<String, Object> addScheduleItem(Scheduleitme scheduleitme);

    public HashMap<String, Object> deleteScheduleItem(Integer scheduleitemId);

    public HashMap<String, Object> modifyScheduleItem(Scheduleitme scheduleitme);


}
