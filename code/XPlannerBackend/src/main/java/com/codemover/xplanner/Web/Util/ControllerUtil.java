package com.codemover.xplanner.Web.Util;

import java.util.Map;

public class ControllerUtil {

    public static Map<String, Object> successHandler(Map<String, Object> response) {
        response.put("errno", 0);
        response.put("errMsg", "");
        return response;
    }


}
