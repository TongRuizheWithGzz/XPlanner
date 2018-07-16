package com.codemover.xplanner.Security.Controller;

import com.codemover.xplanner.Security.Config.MyUserDetailsService;
import com.codemover.xplanner.Security.Exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Objects;

@Controller
@RequestMapping(value = "/api/loginByUsernamePassword")
public class UsernamePasswordLogin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authManager;

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HashMap<String, Object> loginByUsernamePassword(@RequestParam("username") String username,
                                                           @RequestParam("password") String password,
                                                           final HttpServletRequest request)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);


        HashMap<String, Object> response = new HashMap<>();
        response.put("errno", 0);
        response.put("errMsg", "loginByUsernamePassword:ok");
        return response;
    }


}
