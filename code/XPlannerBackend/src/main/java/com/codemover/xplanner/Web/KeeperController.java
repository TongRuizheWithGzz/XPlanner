package com.codemover.xplanner.Web;


import com.codemover.xplanner.Converter.UploadFood.FoodPOJO;
import com.codemover.xplanner.Converter.UploadFood.UploadFoodRequest;
import com.codemover.xplanner.Model.DTO.FoodDTO;
import com.codemover.xplanner.Model.Entity.Food;
import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Service.IKeeperService;
import com.codemover.xplanner.Web.Util.ControllerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class KeeperController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IKeeperService keeperService;

    @PostMapping(value = "/api/keeper")
    Map<String, Object> getUserRecommendation(Principal principal) throws Exception {
        keeperService.setState(principal.getName(), 3, 332);

        HashMap<String, Object> response = new HashMap<>();

        List<KeeperRecommand> keeperRecommands = keeperService.get_keeperRecommands();

        logger.info(new ObjectMapper().writeValueAsString(keeperRecommands));
        response.put("recommands", keeperRecommands);
        return ControllerUtil.successHandler(response);
    }


    @PostMapping(value = "/api/addFood")
    Map<String, Object> addUserEaten(Principal principal, @RequestBody UploadFoodRequest u) throws Exception {
        return ControllerUtil.successHandler(keeperService.addUserFoodEaten(principal.getName(), u.foodPOJOS));
    }

    @GetMapping(value = "/api/food")
    Map<String, Object> getFoodByDinningHall(@RequestParam("diningHall") String diningHall) {
        List<FoodDTO> foods = keeperService.getFoodList(diningHall);
        HashMap<String, Object> response = new HashMap<>();
        response.put("foodInfo", foods);
        return ControllerUtil.successHandler(response);
    }
}
