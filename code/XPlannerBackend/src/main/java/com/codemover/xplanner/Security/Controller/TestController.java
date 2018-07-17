package com.codemover.xplanner.Security.Controller;


import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
public class TestController {


    @RequestMapping(value = "/api/auth/test", method = RequestMethod.GET)
    public HashMap<String, Object> print(Principal principal) {
        System.out.println(principal.getName());
        HashMap<String, Object> response = new HashMap<>();
        SecurityContext sc = SecurityContextHolder.getContext();
        System.out.println("Logged User: " + sc.getAuthentication().getName());
        response.put("errMsg", "ok");
        return response;
    }
}


