package com.codemover.xplanner.Security;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.gson.Gson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", "failed");
        hashMap.put("message", "No Login");
        PrintWriter out = response.getWriter();
        out.write(new Gson().toJson(hashMap));
        System.out.println("AuthenticationEntryPoint!");
        out.flush();
        out.close();
    }

}
