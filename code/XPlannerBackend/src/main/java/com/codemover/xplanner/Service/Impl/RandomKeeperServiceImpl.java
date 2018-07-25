package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Converter.UploadFood.FoodPOJO;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Service.IKeeperService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RandomKeeperServiceImpl implements IKeeperService {
    @Override
    public List<KeeperRecommand> get_keeperRecommands() {
        return null;
    }

    @Override
    public HashMap<String, Object> addUserFoodEaten(String username, LinkedList<FoodPOJO> foodPOJOS) {
        return null;
    }

    @Override
    public void setState(String username, Integer sportsItemNumber, Integer caloriee) {

    }
}
