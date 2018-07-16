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

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public HashMap<String, Object> loginByUsernamePassword(@RequestParam("username") String username,
                                                           @RequestParam("password") String password)
            throws AuthenticationException {
        Authentication auth = authenticate(username, password);
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);


        SecurityContext sc = SecurityContextHolder.getContext();
        if (sc != null) {
            logger.warn("SecurityContextHolder has got an Authentication: '{}'", username);
        }
        sc.setAuthentication(auth);

        HashMap<String, Object> response = new HashMap<>();
        response.put("errno", 0);
        response.put("errMsg", "loginByUsernamePassword:ok");
        return response;
    }


    private Authentication authenticate(String username, String password) {


        try {
            Objects.requireNonNull(username);
            Objects.requireNonNull(password);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException | NullPointerException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
