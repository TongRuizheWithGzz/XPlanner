package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "scheduleitem_from_website", schema = "xplanner", catalog = "")
public class ScheduleitemFromWebsite {
    private int scheduleTimeFromWebsiteId;
    private Timestamp publishTime;
    private String url;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private String address;

    @Id
    @Column(name = "schedule_time_from_website_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getScheduleTimeFromWebsiteId() {
        return scheduleTimeFromWebsiteId;
    }

    public void setScheduleTimeFromWebsiteId(int scheduleTimeFromWebsiteId) {
        this.scheduleTimeFromWebsiteId = scheduleTimeFromWebsiteId;
    }

    @Basic
    @Column(name = "publish_time")
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleitemFromWebsite that = (ScheduleitemFromWebsite) o;
        return scheduleTimeFromWebsiteId == that.scheduleTimeFromWebsiteId &&
                Objects.equals(publishTime, that.publishTime) &&
                Objects.equals(url, that.url) &&
                Objects.equals(description, that.description) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(scheduleTimeFromWebsiteId, publishTime, url, description, startTime, endTime, address);
    }
}
