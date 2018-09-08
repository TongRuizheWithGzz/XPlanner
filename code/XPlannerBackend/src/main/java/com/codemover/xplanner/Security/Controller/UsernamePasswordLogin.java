package com.codemover.xplanner.Security.Controller;

import com.codemover.xplanner.DAO.JAccountUserRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.DAO.WeixinUserRepository;
import com.codemover.xplanner.Model.Entity.JAccountUser;
import com.codemover.xplanner.Model.Entity.Role;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Model.Entity.WeixinUser;
import com.codemover.xplanner.Security.Config.MyUserDetailsService;
import com.codemover.xplanner.Security.Exception.AuthenticationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UsernamePasswordLogin {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JAccountUserRepository jAccountUserRepository;

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


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/auth/loginByWeixin")
    public HashMap<String, Object> loginByWeixin(@RequestParam String openId) {


        HashMap<String, Object> result = new HashMap<>();
        JAccountUser jAccountUser = jAccountUserRepository.findByOpenId(openId);

        //The user hasn't tied the JAccount, tell it to the frontEnd with the openId.
        //Frontend uses the openId to get qrCode and then, authorize JAccount.
        if (jAccountUser == null) {
            result.put("errno", 6);
            result.put("errMsg", "Hasn't tied");
            return result;
        }


        //The user has tied the JAccount, load the principle to security context.
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(jAccountUser.getjAccountName(), jAccountUser.getUniqueId());
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        result.put("errno", 0);
        result.put("errMsg", "Obtain cookie success");
        return result;
        
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/auth/getOpenId")
    public String getOpenId(@RequestParam String code) {
        //This function is to obtain openId of Weixin in exchange for code.
        //Then, use the code to get the openId of the user.
        String weixinCodeUrl = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=wx91d784d2361ebf7c&secret=37634f64467c53b7d7f74f3aeb981a49&js_code=" +
                code + "&grant_type=authorization_code\n";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String strbody = restTemplate.exchange(weixinCodeUrl, HttpMethod.GET, entity, String.class).getBody();

        JSONObject jsonObject = new JSONObject(strbody);
        String openId = jsonObject.getString("openid");
        return openId;
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/api/auth/checkTied")
    public Boolean checkTied(@RequestParam String openId) {
        JAccountUser jAccountUser = jAccountUserRepository.findByOpenId(openId);
        return jAccountUser != null;
    }

}
