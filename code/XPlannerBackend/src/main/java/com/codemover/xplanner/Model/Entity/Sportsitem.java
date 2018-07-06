package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Sportsitem {
    private int sportsItemId;
    private String sportName;
    private Integer caloriePerMinute;
    private String address;

    @Id
    @Column(name = "sportsItem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getSportsItemId() {
        return sportsItemId;
    }

    public void setSportsItemId(int sportsItemId) {
        this.sportsItemId = sportsItemId;
    }

    @Basic
    @Column(name = "sport_name")
    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    @Basic
    @Column(name = "calorie_per_minute")
    public Integer getCaloriePerMinute() {
        return caloriePerMinute;
    }

    public void setCaloriePerMinute(Integer caloriePerMinute) {
        this.caloriePerMinute = caloriePerMinute;
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
        Sportsitem that = (Sportsitem) o;
        return sportsItemId == that.sportsItemId &&
                Objects.equals(sportName, that.sportName) &&
                Objects.equals(caloriePerMinute, that.caloriePerMinute) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sportsItemId, sportName, caloriePerMinute, address);
    }
}
