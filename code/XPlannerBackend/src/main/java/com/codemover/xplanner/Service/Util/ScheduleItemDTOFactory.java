package com.codemover.xplanner.Service.Util;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Security.Exception.ParseProfileJsonException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URLDecoder;

public class ScheduleItemDTOFactory {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleItemDTOFactory.class);

    public static String TimeStamp2String(Timestamp timestamp){
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dFormat.format(new Date(timestamp.getTime()));
    }

    public static ScheduleitmeDTO createScheduleitmeDTOFromScheduleitme(Scheduleitme scheduleitme){
        ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();

        scheduleitmeDTO.title = scheduleitme.getTitle();
        scheduleitmeDTO.description = scheduleitme.getDescription();
        scheduleitmeDTO.hasKnownConcretTime = scheduleitme.isHasKnownConcreteTime();
        scheduleitmeDTO.address = scheduleitme.getAddress();
        scheduleitmeDTO.imageUrl = scheduleitme.getImageUrl();

        String start_time = TimeStamp2String(scheduleitme.getStartTime());
        String end_time = TimeStamp2String(scheduleitme.getEndTime());
        scheduleitmeDTO.startTime = start_time;
        scheduleitmeDTO.endTime = end_time;

        return scheduleitmeDTO;
    }

    public static Set<ScheduleitmeDTO> createScheduleitmeDTOsFromScheduleitmes(Set<Scheduleitme> scheduleitmes){
        Set<ScheduleitmeDTO> scheduleitmeDTOSet = new HashSet<ScheduleitmeDTO>();

        Iterator<Scheduleitme> it = scheduleitmes.iterator();
        while(it.hasNext()){
            ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();
            scheduleitmeDTO = createScheduleitmeDTOFromScheduleitme(it.next());
            scheduleitmeDTOSet.add(scheduleitmeDTO);
        }
        return scheduleitmeDTOSet;
    }

    public static ArrayList<ScheduleitmeDTO> createScheduleitme(String json) {


        JSONObject jsonObject = new JSONObject(json);
        int errno = jsonObject.getInt("error");
        if (errno != 0)
            throw new ParseProfileJsonException(jsonObject.getString("msg"));
        JSONObject resultJsonObject = jsonObject.getJSONObject("result");
        JSONArray acts = resultJsonObject.getJSONArray("acts");

        ArrayList<ScheduleitmeDTO> scheduleitmes = new ArrayList<>();
        for (int index = 0; index < acts.length(); index++) {
            JSONObject act = acts.getJSONObject(index);

            ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();
            scheduleitmeDTO.title = act.getString("name");
            scheduleitmeDTO.address = act.getString("location");/*
            scheduleitmeDTO.startTime = convertStringToTimestamp(act.getString("start_time"), "yyyy-MM-dd HH:mm");
            scheduleitmeDTO.endTime = convertStringToTimestamp(act.getString("end_time"), "yyyy-MM-dd HH:mm");*/

            try {
                scheduleitmeDTO.imageUrl = act.getString("poster");
            } catch (JSONException e) {
                scheduleitmeDTO.imageUrl = null;
                logger.info("missing poster(imageUrl) for this act:'{}'", scheduleitmeDTO.title);
            }
            scheduleitmeDTO.hasKnownConcretTime = false;
            scheduleitmes.add(scheduleitmeDTO);
        }
        return scheduleitmes;
    }

    private static Timestamp convertStringToTimestamp(String strDate, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
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
