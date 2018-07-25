package com.codemover.xplanner.Model.Entity;

import com.codemover.xplanner.Converter.ScheduleitemConverter;
import com.codemover.xplanner.Service.Impl.Spider.SpiderUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

@Entity
public class Notification {

    @JsonIgnore
    private Integer notificationId;
    public String start_time;
    public String end_time;
    public String description;
    public String address;
    public String title;
    public String imageUrl;

    @JsonIgnore
    public String UUID;
    @JsonIgnore
    private Timestamp createTime;


    @Basic
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Notification() {
        start_time = "";
        end_time = "";
        description = "";
        address = "";
        title = "";
        imageUrl = "";
        UUID = "";

    }

    @Override
    public int hashCode() {
        return SpiderUtil.hashCode(start_time + end_time + description + address + title + imageUrl + UUID);
    }


    @Id
    @Column(name = "notification_id")
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp v) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        createTime = ScheduleitemConverter.String2TimeStamp(start_time);

        List<String> strings4 = Arrays.asList(
                title, description, start_time, end_time, address, imageUrl, website, createTime.toString()
        );

        ListIterator<String> listIterator = strings4.listIterator();
        while (listIterator.hasNext()) {
            String s = listIterator.next();

            s = s.replaceAll("'", " ");
            s = s.replaceAll("\n", " ");
            s = s.replaceAll("›", "");
            listIterator.set(s);
        }

        return "insert into notification(notification_id, title, description," +
                " start_time, end_time, " +
                "address, imageUrl, website,create_time) values(" + notificationId.toString() + ",'" + String.join("','", strings4) +
                "');\n";
    }


}





