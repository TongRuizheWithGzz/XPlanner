package com.codemover.xplanner.Converter;


import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ScheduleitemConverter {
    public static Scheduleitme DTOToEntity(@NotNull ScheduleitmeDTO dto) {
        Scheduleitme entity = new Scheduleitme();
        modifyEntity(entity, dto);

        return entity;
    }

    public static ScheduleitmeDTO entityToDTO(@NotNull Scheduleitme entity) {
        ScheduleitmeDTO DTO = new ScheduleitmeDTO();
        DTO.title = entity.getTitle();
        DTO.description = entity.getDescription();
        DTO.address = entity.getAddress();
        DTO.completed = entity.isCompleted();
        DTO.start_time = TimeStamp2String(entity.getStartTime());
        DTO.end_time = TimeStamp2String(entity.getEndTime());
        DTO.scheduleItem_id = entity.getScheduleItmeId();
        return DTO;
    }

    public static void modifyEntity(@NotNull Scheduleitme entity, @NotNull ScheduleitmeDTO dto) {
        entity.setTitle(dto.title);
        entity.setStartTime(String2TimeStamp(dto.start_time));
        entity.setEndTime(String2TimeStamp(dto.end_time));
        entity.setAddress(dto.address);
        entity.setCompleted(dto.completed);
        entity.setDescription(dto.description);
    }

    public static Set<ScheduleitmeDTO> entitiesToDTOs(@NotNull Collection<Scheduleitme> entities) {
        Set<ScheduleitmeDTO> DTOs = new HashSet<ScheduleitmeDTO>();

        Iterator<Scheduleitme> it = entities.iterator();
        while (it.hasNext()) {
            ScheduleitmeDTO scheduleitmeDTO = entityToDTO(it.next());
            DTOs.add(scheduleitmeDTO);
        }

        return DTOs;
    }

    public static String TimeStamp2String(@NotNull Timestamp timestamp) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dFormat.format(new Date(timestamp.getTime()));
    }

    //Timestamp has a precision of yyyy-MM-dd HH:mm:ss
    public static Timestamp String2TimeStamp(@NotNull String strDate) {
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
