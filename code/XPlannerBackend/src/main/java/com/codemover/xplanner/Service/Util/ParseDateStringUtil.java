package com.codemover.xplanner.Service.Util;

import java.util.HashSet;


public class ParseDateStringUtil {

    public static Integer parseDay(String dayString) throws NumberFormatException {
        Integer result = 0;
        try {
            return Integer.parseInt(dayString);
        } catch (NumberFormatException e) {
            try {

                String validToken = parseChineseStringInHundred(dayString);
                DateUtil dateUtil = new DateUtil();
                return dateUtil.String2Int(validToken);
            } catch (NumberFormatException e2) {
                throw e2;
            }

        }
    }

    private static String parseChineseStringInHundred(String ChineseNumber) throws NumberFormatException {
        if (ChineseNumber.length() > 3 || ChineseNumber.length() <= 0)
            throw new NumberFormatException("Chinese number length invalid!");


        HashSet<String> strings = new HashSet<String>() {{
            add("一");
            add("二");
            add("三");
            add("四");
            add("五");
            add("六");
            add("七");
            add("八");
            add("九");
        }};

        switch (ChineseNumber.length()) {
            case (1): {
                if (strings.contains(ChineseNumber) || ChineseNumber.equals("十"))
                    return ChineseNumber;
            }
            case (2): {
                String ten = ChineseNumber.substring(0, 1);
                String one = ChineseNumber.substring(1, 2);
                if (ten.equals("十") && strings.contains(one))
                    return ChineseNumber;
                if (strings.contains(ten) && one.equals("十"))
                    return ChineseNumber;
                else
                    throw new NumberFormatException("Parse Chinese of length 2 error");
            }
            case (3): {
                String hundred = ChineseNumber.substring(0, 1);
                String ten = ChineseNumber.substring(1, 2);
                String one = ChineseNumber.substring(2, 3);
                if (strings.contains(hundred) && ten.equals("十") && strings.contains(one))
                    return ChineseNumber;
                else
                    throw new NumberFormatException("Parse Chinese of length 3 error");
            }
            default:
                break;
        }
        throw new NumberFormatException("Unknowing error parsing Chinese number");
    }

}
