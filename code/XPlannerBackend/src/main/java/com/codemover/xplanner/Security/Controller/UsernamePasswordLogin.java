package com.codemover.xplanner.Security.Controller;

import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Security.Config.MyUserDetailsService;
import com.codemover.xplanner.Security.Exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;

@Controller
public class UsernamePasswordLogin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authManager;

    @ResponseBody
    @RequestMapping(value = "/api/auth/loginByUsernamePassword", method = RequestMethod.POST)
    public HashMap<String, Object> loginByUsernamePassword(@RequestParam("username") String username,
                                                           @RequestParam("password") String password,
                                                           final HttpServletRequest request)
            throws AuthenticationException {
        logger.info("LoginByUsernamePassword, username:'{}',password:'{}'", username, password);
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

    @ResponseBody
    @RequestMapping(value = "/api/auth/checkSession", method = RequestMethod.GET)
    public HashMap<String, Object> checkSession(Principal principal) {
        HashMap<String, Object> result = new HashMap<>();

        String username = principal.getName();
        User user = userRepository.findByUserName(username);
        if (user != null) {
            result.put("errno", 0);
            result.put("errMsg", "checkSession:have logined");
        } else {
            result.put("errno", 1);
        }
        return result;
    }
}
