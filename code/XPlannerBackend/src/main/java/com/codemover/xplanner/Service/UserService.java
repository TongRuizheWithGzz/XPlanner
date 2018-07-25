package com.codemover.xplanner.Service;

import java.util.HashMap;

public interface UserService {

     HashMap<String, Object> getUserInfo(String username);

     HashMap<String,Object> getUserSettings(String username);
}

