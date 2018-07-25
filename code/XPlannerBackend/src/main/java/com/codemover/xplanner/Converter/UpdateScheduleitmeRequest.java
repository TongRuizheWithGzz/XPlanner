package com.codemover.xplanner.Converter;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;

import java.io.Serializable;

public class UpdateScheduleitmeRequest implements Serializable {
    private static final long serialVersionUID = 1350166508152483573L;

    public String start_time;
    public String end_time;
    public String description;
    public String address;
    public String title;
    public Integer year;
    public Integer month;
    public Integer day;
    public boolean completed;

    public String getStart_time() {
        return start_time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public ScheduleitmeDTO toScheduleitemDTO() {
        return new ScheduleitmeDTO(
                this.start_time,
                this.end_time,
                this.description,
                this.address,
                this.title,
                this.completed
        );
    }
}
