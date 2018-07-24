package com.codemover.xplanner.Model.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class ScheduleitmeDTO implements Serializable {
    private static final long serialVersionUID = 1250166508152483573L;

    public ScheduleitmeDTO() {
        super();
    }

    public ScheduleitmeDTO(String start_time,
                           String end_time,
                           String description,
                           String address,
                           String title,
                           boolean completed) {

        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.address = address;
        this.title = title;
        this.completed = completed;
    }

    public Integer getScheduleItem_id() {
        return scheduleItem_id;
    }

    public void setScheduleItem_id(Integer scheduleItem_id) {
        this.scheduleItem_id = scheduleItem_id;
    }

    public String start_time;
    public String end_time;
    public String description;
    public String address;
    public String title;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Integer scheduleItem_id;

    public boolean completed;


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}




