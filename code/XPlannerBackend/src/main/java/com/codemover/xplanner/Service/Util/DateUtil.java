package com.codemover.xplanner.Service.Util;

import java.util.HashMap;

public class DateUtil {
    private ChineseTool chineseTool = new ChineseTool();

    public HashMap<String, String> default_time(String hint) {
        return chineseTool.default_time(hint);
    }

    public int String2Int(String hint) {
        if (hint.length() == 2 && hint.charAt(hint.length() - 2) == '十') {
            hint = "一" + hint;
        }
        int value = 0;
        int sectionNumber = 0;
        for (int i = 0; i < hint.length(); i++) {
            int v = chineseTool.getIntMap().get(hint.charAt(i));
            if (v == 10 && !(hint.length() == 1)) {
                sectionNumber = v * sectionNumber;
                value = value + sectionNumber;
            } else if (i == hint.length() - 1) {
                value = value + v;
            } else {
                sectionNumber = v;
            }
        }
        return value;
    }
}
