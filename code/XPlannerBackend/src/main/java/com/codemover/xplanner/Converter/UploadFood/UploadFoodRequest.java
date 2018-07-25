package com.codemover.xplanner.Converter.UploadFood;

import java.util.LinkedList;

public class UploadFoodRequest {

    public LinkedList<FoodPOJO> foodPOJOS;

    public LinkedList<FoodPOJO> getFoodPOJOS() {
        return foodPOJOS;
    }

    public void setFoodPOJOS(LinkedList<FoodPOJO> foodPOJOS) {
        this.foodPOJOS = foodPOJOS;
    }
}
