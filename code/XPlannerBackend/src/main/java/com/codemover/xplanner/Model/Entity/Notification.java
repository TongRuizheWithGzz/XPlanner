package com.codemover.xplanner.Model.Entity;

import com.codemover.xplanner.Service.Impl.Spider.SpiderUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Notification {

    private Integer notificationId;
    public String start_time;
    public String end_time;
    public String description;
    public String address;
    public String title;
    public String imageUrl;
    private Timestamp create_time;

    public Notification() {
        start_time = "";
        end_time = "";
        description = "";
        address = "";
        title = "";
        imageUrl = "";

    }

    @Override
    public int hashCode() {
        return SpiderUtil.hashCode(start_time + end_time + description + address + title + imageUrl);
    }


    @Id
    @Column(name = "notification_id")
    @JsonIgnore
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }


    @Basic
    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    @Basic
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Basic
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String website;

    @Basic
    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Basic
    @Column(name="create_time")
    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}





