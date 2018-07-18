package com.codemover.xplanner.Web;

import com.codemover.xplanner.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping(value = "/api/schedule/all")
    public HashMap<String, Object> queryAllScheduleitems(Principal principal) {
        String username = principal.getName();
        return scheduleService.findUserSchedule(username);
    }

}
