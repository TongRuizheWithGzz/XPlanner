package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public HashMap<String, Object> getUserInfo(String username) {
        HashMap<String, Object> result = new HashMap<>();
        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Query user's getUserInfo: user not found");
        }
        result.put("userInfo", user);
        return result;
    }

    @Override
    public HashMap<String, Object> getUserSettings(String username) {
        HashMap<String, Object> result = new HashMap<>();
        User user = userRepository.findByUserName(username);
        if (user == null) {
            logger.warn("No such user: '{}',will ignore", username);
            throw new NullPointerException("Query user's getUserSettings: user not found");
        }
        result.put("userSettings", user.getPlannerstores());
        return result;
    }
}
