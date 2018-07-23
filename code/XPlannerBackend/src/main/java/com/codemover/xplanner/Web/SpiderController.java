package com.codemover.xplanner.Web;

import com.codemover.xplanner.Service.SpiderService;
import com.codemover.xplanner.Web.Util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class SpiderController {

    @Autowired
    SpiderService spiderService;


    @GetMapping(value = "/api/notifications")
    Map<String, Object> getNotificationsFromWebsite(@RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("size") Integer size) throws Exception {
        return ControllerUtil.successHandler(spiderService.getInfoFromWebsites(pageNumber, size));

    }




}
