package com.codemover.xplanner.Service;


import com.codemover.xplanner.Converter.UpdateScheduleitmeRequest;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;

import java.text.ParseException;
import java.util.HashMap;

public interface ScheduleService {

    public HashMap<String, Object> findUserSchedule(String username);

    public HashMap<String, Object> findSchedule4OneDay(String username, int year, int month, int day);

    public HashMap<String, Object> addScheduleItem(UpdateScheduleitmeRequest request, String username);

    public HashMap<String, Object> deleteScheduleItem(Integer scheduleitemId, String username);

    public HashMap<String, Object> updateScheduleItem(Integer scheduleitemId, UpdateScheduleitmeRequest request, String username);

    public HashMap<String, Object> getScheduledDays(String username, int year, int month) throws ParseException;


}
