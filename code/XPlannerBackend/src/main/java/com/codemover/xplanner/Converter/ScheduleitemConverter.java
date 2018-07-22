package com.codemover.xplanner.Converter;


import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleitemConverter {
    public static ScheduleitmeDTO createScheduleitmeDTOFromScheduleitme(@NotNull Scheduleitme scheduleitme) {
        ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();


        scheduleitmeDTO.title = scheduleitme.getTitle();
        scheduleitmeDTO.description = scheduleitme.getDescription();
        scheduleitmeDTO.hasKnownConcreteTime = scheduleitme.isHasKnownConcreteTime();
        scheduleitmeDTO.address = scheduleitme.getAddress();
        scheduleitmeDTO.completed = scheduleitme.isCompleted();
        scheduleitmeDTO.imageUrl = scheduleitme.getImageUrl();
        String start_time = TimeStamp2String(scheduleitme.getStartTime());
        String end_time = TimeStamp2String(scheduleitme.getEndTime());
        scheduleitmeDTO.start_time = start_time;
        scheduleitmeDTO.end_time = end_time;
        return scheduleitmeDTO;

    }
    private static String TimeStamp2String(Timestamp timestamp) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dFormat.format(new Date(timestamp.getTime()));
    }
}
