package com.codemover.xplanner.Service.Util;

import java.util.HashMap;

public class DateUtil {
    private ChineseTool chineseTool = new ChineseTool();

    public HashMap<String, String> default_time(String hint) {
        return chineseTool.default_time(hint);
    }

    public int String2Int(String hint) { return chineseTool.String2Int(hint); }
}
