package com.codemover.xplanner.Service.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ChineseTool {

    public  ChineseTool(){
        contructIntMap();
        contructTimeMap();
    }

    private HashMap<Character,Integer> intMap = new HashMap<>();
    public static char[] chnNumChar = {'零','一','二','三','四','五','六','七','八','九'};
    private void contructIntMap(){
        for(int i=0;i<chnNumChar.length;i++){
            intMap.put(chnNumChar[i],i);
        }
        intMap.put('十',10);
    }
    public int String2Int(String hint) {
        if (hint.length() == 2 && hint.charAt(hint.length() - 2) == '十') {
            hint = "一" + hint;
        }
        int value = 0;
        int sectionNumber = 0;
        for (int i = 0; i < hint.length(); i++) {
            int v = intMap.get(hint.charAt(i));
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

    private HashMap<String,Integer> timeMap = new HashMap<>();
    private void contructTimeMap(){
        timeMap.put("今日",0);
        timeMap.put("今天",0);
        timeMap.put("明天",1);
        timeMap.put("明日",1);
        timeMap.put("后天",2);
        timeMap.put("本周一",3);
        timeMap.put("周一",3);
        timeMap.put("这周一",3);
        timeMap.put("本周二",4);
        timeMap.put("周二",4);
        timeMap.put("这周二",4);
        timeMap.put("本周三",5);
        timeMap.put("周三",5);
        timeMap.put("这周三",5);
        timeMap.put("本周四",6);
        timeMap.put("周四",6);
        timeMap.put("这周四",6);
        timeMap.put("本周五",7);
        timeMap.put("周五",7);
        timeMap.put("这周五",7);
        timeMap.put("本周六",8);
        timeMap.put("周六",8);
        timeMap.put("这周六",8);
        timeMap.put("本周日",9);
        timeMap.put("周日",9);
        timeMap.put("这周日",9);
        timeMap.put("周天",9);
        timeMap.put("下周一",10);
        timeMap.put("下周二",11);
        timeMap.put("下周三",12);
        timeMap.put("下周四",13);
        timeMap.put("下周五",14);
        timeMap.put("下周六",15);
        timeMap.put("下周日",16);
        timeMap.put("下周天",16);
        timeMap.put("待会",17);
        timeMap.put("待会儿",17);
        timeMap.put("一会儿",17);
        timeMap.put("昨天",18);
        timeMap.put("昨日",18);
        timeMap.put("明天早上",19);
        timeMap.put("明天上午",20);
    }
    public HashMap<String,String> default_time(String hint){
        HashMap<String,String> response = new HashMap<>();
        if(!timeMap.containsKey(hint)){
            response.put("errmsg","404");
            return response;
        }

        int index = timeMap.get(hint);
        String start_time = "";
        String end_time="";
        Calendar calendar = Calendar.getInstance();

        //<--------------------- get time ---------------------->
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        switch (index){
            case 0:
                calendar.set(year,month,day,12,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day,13,00,0);
                end_time = df.format(calendar.getTime());
                break;
            case 1:
                calendar.set(year,month,day+1,12,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day+1,13,00,0);
                end_time = df.format(calendar.getTime());
                break;
            case 2:
                calendar.set(year,month,day+2,12,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day+2,13,00,0);
                end_time = df.format(calendar.getTime());
                break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                int this_Monday = day-(day_of_week-2);
                int that_day = this_Monday + (index-3);
                calendar.set(year,month,that_day,12,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,that_day,13,00,0);
                end_time = df.format(calendar.getTime());
                break;
            case 17:
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                calendar.set(year,month,day,hour,minute+30,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day,hour+1,minute+30,0);
                end_time = df.format(calendar.getTime());
                break;
            case 18:
                calendar.set(year,month,day-1,12,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day-1,13,00,0);
                end_time = df.format(calendar.getTime());
                break;
            case 19:
                calendar.set(year,month,day-1,7,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day-1,8,00,0);
                end_time = df.format(calendar.getTime());
                break;
            case 20:
                calendar.set(year,month,day-1,9,00,0);
                start_time = df.format(calendar.getTime());
                calendar.set(year,month,day-1,10,00,0);
                end_time = df.format(calendar.getTime());
                break;
                default:break;
        }
        response.put("start_time",start_time);
        response.put("end_time",end_time);
        response.put("errmsg","200");
        return response;
    }

}
