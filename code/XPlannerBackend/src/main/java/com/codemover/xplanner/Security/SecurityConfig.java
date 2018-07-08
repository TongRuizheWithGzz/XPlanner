package com.codemover.xplanner.Security;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                exceptionHandling().
                accessDeniedHandler(accessDeniedHandler).
                authenticationEntryPoint(authenticationEntryPoint)

                .and()

                .authorizeRequests()
                .antMatchers("/user/checkLogin").hasAnyRole("USER", "ADMIN")
                .antMatchers("/book/update").hasRole("ADMIN")
                .antMatchers("/book/**").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/login_p").permitAll()
                .antMatchers("/cart/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/order/all/admin").hasRole("ADMIN")
                .antMatchers("/order/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/images/book/**").permitAll()
                .antMatchers("/images/userAvatar/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/images/userUploadBook/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/user/profile").hasRole("USER")
                .antMatchers("/upload/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login_page")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        String username = authentication.getName();
                        List<String> authorities = new ArrayList<>();
                        for (GrantedAuthority authority : authentication.getAuthorities()) {
                            authorities.add(authority.getAuthority());
                        }
                        PrintWriter out = httpServletResponse.getWriter();
                        out.flush();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status", "success");
                        hashMap.put("message", "login success");
                        hashMap.put("authorities", authorities);
                        String resultJson = new Gson().toJson(hashMap);
                        out.write(resultJson);
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status", "failed");
                        hashMap.put("message", "Username or password is wrong!");
                        String resultJson = new Gson().toJson(hashMap);
                        out.write(resultJson);
                        out.flush();
                        out.close();
                    }
                }).loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        String username = authentication.getName();

                        PrintWriter out = httpServletResponse.getWriter();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("status", "success");
                        hashMap.put("message", "logout success");
                        String resultJson = new Gson().toJson(hashMap);
                        out.write(resultJson);
                        out.flush();
                        out.close();
                    }
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .deleteCookies("login")
                .deleteCookies("role")
                .deleteCookies("username")

                .permitAll()
                .and().csrf().disable().cors()
                .and()
                .httpBasic();


    }
}
