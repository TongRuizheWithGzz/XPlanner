package com.codemover.xplanner.Web;


import com.codemover.xplanner.Converter.UploadFood.FoodPOJO;
import com.codemover.xplanner.Converter.UploadFood.UploadFoodRequest;
import com.codemover.xplanner.Model.DTO.FoodDTO;
import com.codemover.xplanner.Model.DTO.RecommandDTO;
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
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class KeeperController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IKeeperService keeperService;

    @PostMapping(value = "/api/keeper")
    Map<String, Object> getUserRecommendation(Principal principal) throws Exception {
        keeperService.setState(principal.getName(), 3);

        HashMap<String, Object> response = new HashMap<>();

        List<KeeperRecommand> keeperRecommands = keeperService.get_keeperRecommands();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<RecommandDTO> recommandDTOList = new LinkedList<>();
        for(KeeperRecommand keeperRecommand:keeperRecommands){
            RecommandDTO recommandDTO = new RecommandDTO();
            recommandDTO.setStart_time(df.format(keeperRecommand.getStart_time()));
            recommandDTO.setEnd_time(df.format(keeperRecommand.getEnd_time()));
            recommandDTO.setAddress(keeperRecommand.getAddress());
            recommandDTO.setTitle(keeperRecommand.getTitle());
            recommandDTO.setDescription(keeperRecommand.getDescription());
            recommandDTOList.add(recommandDTO);
        }

        response.put("recommands", recommandDTOList);

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
