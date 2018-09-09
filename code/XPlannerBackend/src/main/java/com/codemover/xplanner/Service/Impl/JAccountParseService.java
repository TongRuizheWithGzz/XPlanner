package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.DAO.ScheduleItemRepository;
import com.codemover.xplanner.Model.Entity.JAccountUser;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JAccountParseService {


    @Autowired
    ScheduleItemRepository scheduleItemRepository;


    //title,description,address,start_time,end_time
    //need string of first_week
    public void JAccountLesson(String Tojson, User user) throws JSONException, ParseException {

        Set<Scheduleitme> scheduleitmes = new HashSet<>();


        String first_week = "2018-9-10";

        JSONObject json = new JSONObject(Tojson);
        JSONArray entities = json.getJSONArray("entities");
        for (int i = 0; i < entities.length(); i++) {


            JSONObject entity = entities.getJSONObject(i);

            String title = entity.getString("name");
            String description = entity.getString("code");
            JSONArray classes = entity.getJSONArray("classes");
            for (int j = 0; j < classes.length(); j++) {
                Scheduleitme s = new Scheduleitme();


                JSONObject clas = classes.getJSONObject(j);

                JSONObject schedule = clas.getJSONObject("schedule");
                int week = schedule.getInt("week");
                int day = schedule.getInt("day");
                int period = schedule.getInt("period");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dt_firstweek = simpleDateFormat.parse(first_week);
                Calendar c_firstweek = Calendar.getInstance();
                c_firstweek.setTime(dt_firstweek);
                c_firstweek.add(Calendar.DAY_OF_YEAR, day - 1 + (week - 1) * 7);
                Date dt = c_firstweek.getTime();

                String dateString = simpleDateFormat.format(dt);

                String startTime = "";
                String endTime = "";

                switch (period) {
                    case 1:
                        startTime = dateString + " 08:00:00";
                        endTime = dateString + " 08:45:00";
                        break;
                    case 2:
                        startTime = dateString + " 08:55:00";
                        endTime = dateString + " 09:40:00";
                        break;
                    case 3:
                        startTime = dateString + " 10:00:00";
                        endTime = dateString + " 10:45:00";
                        break;
                    case 4:
                        startTime = dateString + " 10:55:00";
                        endTime = dateString + " 11:40:00";
                        break;
                    case 5:
                        startTime = dateString + " 12:00:00";
                        endTime = dateString + " 12:45:00";
                        break;
                    case 6:
                        startTime = dateString + " 12:55:00";
                        endTime = dateString + " 13:40:00";
                        break;
                    case 7:
                        startTime = dateString + " 14:00:00";
                        endTime = dateString + " 14:45:00";
                        break;
                    case 8:
                        startTime = dateString + " 14:55:00";
                        endTime = dateString + " 15:40:00";
                        break;
                    case 9:
                        startTime = dateString + " 16:00:00";
                        endTime = dateString + " 16:45:00";
                        break;
                    case 10:
                        startTime = dateString + " 16:55:00";
                        endTime = dateString + " 17:40:00";
                        break;
                    case 11:
                        startTime = dateString + " 18:00:00";
                        endTime = dateString + " 18:45:00";
                        break;
                    case 12:
                        startTime = dateString + " 18:55:00";
                        endTime = dateString + " 19:40:00";
                        break;
                    case 13:
                        startTime = dateString + " 20:00:00";
                        endTime = dateString + " 20:45:00";
                        break;
                    case 14:
                        startTime = dateString + " 20:55:00";
                        endTime = dateString + " 21:40:00";
                        break;
                    default:
                        break;
                }

                SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.sql.Timestamp start_time = new java.sql.Timestamp(DateFormat.parse(startTime).getTime());
                java.sql.Timestamp end_time = new java.sql.Timestamp(DateFormat.parse(endTime).getTime());

                System.out.println(start_time);
                System.out.println(end_time);

                JSONObject classroom = clas.getJSONObject("classroom");
                String address = classroom.getString("name");
                if (address.equals(".")) {
                    address = "暂时未定";
                }

                //scheduleitem.set
                s.setTitle(title);
                s.setStartTime(start_time);
                s.setEndTime(end_time);
                s.setDescription(description);
                s.setAddress(address);
                s.setCompleted(true);

                s.setUser(user);
                scheduleitmes.add(s);
            }
        }
        scheduleItemRepository.saveAll(scheduleitmes);
    }

    //title,description,start_time,end_time,address
    public void JAccountExam(String Tojson) throws ParseException, JSONException {

        JSONObject json = new JSONObject(Tojson);
        JSONArray entities = json.getJSONArray("entities");
        for (int i = 0; i < entities.length(); i++) {
            JSONObject entity = entities.getJSONObject(i);
            String note = entity.getString("note");
            if (note.equals("")) continue;
            String date = note.substring(0, 10);
            String startTime = note.substring(note.length() - 11, note.length() - 9) + ":" + note.substring(note.length() - 8, note.length() - 6) + ":00";
            startTime = date + " " + startTime;
            String endTime = note.substring(note.length() - 5, note.length() - 3) + ":" + note.substring(note.length() - 2) + ":00";
            endTime = date + " " + endTime;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.sql.Timestamp start_time = new java.sql.Timestamp(simpleDateFormat.parse(startTime).getTime());
            java.sql.Timestamp end_time = new java.sql.Timestamp(simpleDateFormat.parse(endTime).getTime());

            JSONObject classroom = entity.getJSONObject("classroom");
            String address = classroom.getString("name");

            JSONObject schedule = entity.getJSONObject("schedule");
            int week = schedule.getInt("week");
            int day = schedule.getInt("day");

            JSONObject lessonbasic = entity.getJSONObject("lessonbasic");
            String title = lessonbasic.getString("name");
            String description = lessonbasic.getString("code");
            System.out.println(title + " " + description);
        }


    }


}
