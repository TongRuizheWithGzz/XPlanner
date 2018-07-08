package com.codemover.xplanner.Controller;

import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping(value="/user/all")
    @ResponseBody
    public List<User> queryAllUser(){
        User user = new User();
        user.setUserName("XPlanner");
        user.setUserPassword("123");
        userService.save(user);

        return userService.queryAllUser();
    }
}
