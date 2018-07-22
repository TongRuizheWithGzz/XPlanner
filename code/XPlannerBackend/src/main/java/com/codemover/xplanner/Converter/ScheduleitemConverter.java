package com.codemover.xplanner.Converter;


import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ScheduleitemConverter {
    public static Scheduleitme DTOToEntity(@NotNull ScheduleitmeDTO dto) {
        Scheduleitme entity = new Scheduleitme();
        entity.setTitle(dto.title);
        entity.setImageUrl(dto.imageUrl);
        entity.setStartTime(String2TimeStamp(dto.start_time));
        entity.setEndTime(String2TimeStamp(dto.end_time));
        entity.setAddress(dto.address);
        entity.setCompleted(dto.completed);
        entity.setDescription(dto.description);
        return entity;
    }

    public static ScheduleitmeDTO entityToDTO(@NotNull Scheduleitme entity) {
        ScheduleitmeDTO DTO = new ScheduleitmeDTO();
        DTO.title = entity.getTitle();
        DTO.description = entity.getDescription();
        DTO.address = entity.getAddress();
        DTO.completed = entity.isCompleted();
        DTO.imageUrl = entity.getImageUrl();
        DTO.start_time = TimeStamp2String(entity.getStartTime());
        DTO.end_time = TimeStamp2String(entity.getEndTime());

        return DTO;
    }


    public static Set<ScheduleitmeDTO> entitiesToDTOs(@NotNull Set<Scheduleitme> entities) {
        Set<ScheduleitmeDTO> DTOs = new HashSet<ScheduleitmeDTO>();

        Iterator<Scheduleitme> it = entities.iterator();
        while (it.hasNext()) {
            ScheduleitmeDTO scheduleitmeDTO = entityToDTO(it.next());
            DTOs.add(scheduleitmeDTO);
        }

        return DTOs;
    }

    private static String TimeStamp2String(@NotNull Timestamp timestamp) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dFormat.format(new Date(timestamp.getTime()));
    }

    //Timestamp has a precision of yyyy-MM-dd HH:mm:ss
    private static Timestamp String2TimeStamp(@NotNull String strDate) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }


}