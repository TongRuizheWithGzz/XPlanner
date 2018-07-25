package com.codemover.xplanner.Service;

import com.codemover.xplanner.Model.Entity.KeeperRecommand;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface IKeeperService {

    List<KeeperRecommand> get_keeperRecommands();

    HashMap<String, Object> addUserFoodEaten(String username, String food_name, int caloriee);

    public void setState(String username, Integer sportsItemNumber, Integer caloriee);

}
