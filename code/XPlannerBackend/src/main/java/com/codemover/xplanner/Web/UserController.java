package com.codemover.xplanner.Web;

import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Service.UserService;
import com.codemover.xplanner.Web.Util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/api/me")
    public Map<String, Object> getUserInfo(Principal principal) {
        return ControllerUtil.successHandler(userService.getUserInfo(principal.getName()));
    }

    @GetMapping(value = "/api/me/settings")
    public Map<String, Object> getUserSettings(Principal principal) {
        return ControllerUtil.successHandler(userService.getUserSettings(principal.getName()));
    }


}
