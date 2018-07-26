package com.codemover.xplanner.Model.DTO;

import com.codemover.xplanner.Model.Entity.Food;

public class FoodDTO {
    public String food_name;
    public String dininghall;
    public Integer calorie;
    public Integer food_id;
    public Integer food_type_id;

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getDininghall() {
        return dininghall;
    }

    public void setDininghall(String dininghall) {
        this.dininghall = dininghall;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public Integer getFood_id() {
        return food_id;
    }

    public void setFood_id(Integer food_id) {
        this.food_id = food_id;
    }

    public Integer getFood_type_id() {
        return food_type_id;
    }

    public void setFood_type_id(Integer food_type_id) {
        this.food_type_id = food_type_id;

    }
}
