package com.codemover.xplanner.Service;

import com.codemover.xplanner.Converter.UploadFood.FoodPOJO;
import com.codemover.xplanner.Model.DTO.FoodDTO;
import com.codemover.xplanner.Model.Entity.Food;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public interface IKeeperService {

    List<KeeperRecommand> get_keeperRecommands() throws ParseException;

    HashMap<String, Object> addUserFoodEaten(String username, LinkedList<FoodPOJO> foodPOJOS);

    public void setState(String username, Integer sportsItemNumber);

    List<FoodDTO> getFoodList(String dininghall);

    List<HashMap<String, Object>> getTodayFood(String username);
}

