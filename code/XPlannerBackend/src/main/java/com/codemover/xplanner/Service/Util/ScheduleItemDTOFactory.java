package com.codemover.xplanner.Service.Util;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Security.Exception.ParseProfileJsonException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ScheduleItemDTOFactory {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleItemDTOFactory.class);

    private static String tongquDetailApiUrl;

    public static void setTongquDetailApiUrl(String tongquDetailApiUrl) {
        ScheduleItemDTOFactory.tongquDetailApiUrl = tongquDetailApiUrl;
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
            Integer actId = act.getInt("actid");


            String url = ScheduleItemDTOFactory.buildUrlForActDetail(actId, tongquDetailApiUrl);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response
                    = restTemplate.getForEntity(url, String.class);
            String detailJson = response.getBody();


            JSONObject detailJsonObject = new JSONObject(detailJson);
            JSONArray jsonArray = detailJsonObject.getJSONArray("actContents");
            String description = "";
            for (int detailIndex = 0; detailIndex < jsonArray.length(); detailIndex++) {
                JSONObject detail = jsonArray.getJSONObject(detailIndex);
                if (description != null && description.length() > 1024)
                    break;
                boolean hasTest = detail.getBoolean("textIf");
                if (hasTest) {
                    description += detail.getString("text");
                }
            }


            scheduleitmeDTO.description = description;
            scheduleitmeDTO.title = act.getString("name");
            scheduleitmeDTO.address = act.getString("location");
            scheduleitmeDTO.start_time = act.getString("start_time");
            scheduleitmeDTO.end_time = act.getString("end_time");

            try {
                scheduleitmeDTO.imageUrl = act.getString("poster");
            } catch (JSONException e) {
                scheduleitmeDTO.imageUrl = null;
                logger.info("missing poster(imageUrl) for this act:'{}'", scheduleitmeDTO.title);
            }
            scheduleitmeDTO.hasKnownConcreteTime = true;
            scheduleitmes.add(scheduleitmeDTO);
        }

        return scheduleitmes;
    }


    //Timestamp has a precision of yyyy-MM-dd HH:mm:ss
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

    public static String TimeStamp2String(Timestamp timestamp) {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dFormat.format(new Date(timestamp.getTime()));
    }

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


    public static Set<ScheduleitmeDTO> createScheduleitmeDTOsFromScheduleitmes(Set<Scheduleitme> scheduleitmes) {
        Set<ScheduleitmeDTO> scheduleitmeDTOSet = new HashSet<ScheduleitmeDTO>();

        Iterator<Scheduleitme> it = scheduleitmes.iterator();
        while (it.hasNext()) {
            ScheduleitmeDTO scheduleitmeDTO =
                    createScheduleitmeDTOFromScheduleitme(it.next());
            scheduleitmeDTOSet.add(scheduleitmeDTO);
        }


        return scheduleitmeDTOSet;
    }

    public static String buildUrlForActDetail(Integer actId, String tongquActDetailUrl) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tongquActDetailUrl)
                .queryParam("id", actId);
        return builder.toUriString();

    }

    public static String buildUrl(Integer offset, String orderBy, String tongquBaseUrl) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tongquBaseUrl)
                .queryParam("type", 0)
                .queryParam("status", 0)
                .queryParam("order", orderBy)
                .queryParam("offset", offset);
        return builder.toUriString();
    }

}