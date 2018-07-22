package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Service.Exception.ParseScheduleitmeJsonException;
import com.codemover.xplanner.Service.Util.ScheduleItemDTOFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TongquXSpider {

    @Value("${api.tongqu.url}")
    private String tongquApiUrl;

    @Value("${api.tongqu.detail}")
    private String tongquDetailApiUrl;

    public HashMap<String, Object> getScheduleitemsFromTongqu(Integer offset, String orderBy) {
        HashMap<String, Object> response = new HashMap<>();
        String responseJson = getActsFromTongqu(offset, orderBy);
        try {
            ScheduleItemDTOFactory.setTongquDetailApiUrl(tongquDetailApiUrl);
            ArrayList<ScheduleitmeDTO> scheduleitmeDTOS = ScheduleItemDTOFactory.createScheduleitme(responseJson);
            response.put("errno", 0);
            response.put("errMsg", "tongqu acts get:ok");
            response.put("scheduleitmes", scheduleitmeDTOS);
            return response;
        } catch (ParseScheduleitmeJsonException e) {
            e.printStackTrace();
            response.put("errno", 1);
            response.put("errMsg", "tongqu acts get:failed");
            return response;
        }

    }


    public String getActsFromTongqu(Integer offset, String orderBy) {
        String url = ScheduleItemDTOFactory.buildUrl(offset, orderBy, tongquApiUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }


    public String getActDetailByIdFromWeSJTU(Integer actId) {
        String url = ScheduleItemDTOFactory.buildUrlForActDetail(actId, tongquDetailApiUrl);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }


}