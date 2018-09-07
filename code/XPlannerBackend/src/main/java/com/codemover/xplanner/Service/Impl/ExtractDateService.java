package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Service.Util.ChineseTool;
import com.codemover.xplanner.Service.Util.ParseDateStringUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExtractDateService {

    private Pattern p;
    private Matcher m;
    private List<String> fixedStrings;
    private List<String> fixedPlaces;
    private ChineseTool chineseTool;


    private String month;
    private String day;
    private String section;
    private String hour;
    private String minute;
    private String otherHint;
    private String in;
    private String halfHour;
    private String place;

    public void clean(){
        month=null;
        day=null;
        section=null;
        hour=null;
        minute=null;
        otherHint=null;
        in=null;
        halfHour=null;

    }

    public void cleanPlace(){
        place = "";
    }


    public void replaceInput(Matcher m, String replacement) {
        this.in = m.replaceAll(replacement);
    }

    public ExtractDateService() {


        chineseTool = new ChineseTool();
        fixedStrings = new LinkedList<>();
        fixedPlaces = new LinkedList<>();

        List<String> places = Arrays.asList("光明体育场","光体","光明网球场","菁菁堂","程及美术馆","致远游泳馆","行政楼","学生活动中心","包玉刚图书馆","包图","上院","中院","下院","第一餐饮大楼","一餐","华联","玉兰苑",
                "力学楼","物理楼","数学楼","土建楼","材料楼","信控楼","能源楼","激光制造","环境楼","印刷厂","工程训练中心","总务楼","第四餐饮大楼","四餐","新体","霍英东体育馆","铁生馆","人武部","校医院","留园","大智居","第三餐饮大楼","三餐","光彪楼","第二餐饮大楼","二餐","逸夫科技楼","学生服务中心","光彪楼北面","光彪楼北侧","新图","陈瑞球楼","法学院","媒设学院",
                "新图书馆","杨咏曼楼","人文学院","行政楼","医学楼","电子信息与电气工程学院","电院","电群","软院演播厅","软件大楼","软院","微电子学院","空天楼","密西根学院","密歇更学院","密院","农学大楼","生物药学楼","船海建工学院大楼","综合实验楼","机械动力大楼","海洋深水试验池");
        fixedPlaces.addAll(places);

        List<String> strings4 = Arrays.asList(
                "周一", "周一", "周二", "周三", "周四", "周五", "周六", "周日"
        );
        for (String s : strings4) {
            fixedStrings.add("本" + s);
        }
        for (String s : strings4) {
            fixedStrings.add("下" + s);
        }


        fixedStrings.addAll(strings4);


        List<String> strings1 = Arrays.asList(
                "今天", "昨天", "明天", "后天",
                "今日", "昨日", "明日");
        List<String> strings2 = Arrays.asList(
                "早晨", "早上", "上午",
                "中午",
                "下午",
                "晚上"
        );


        LinkedList<String> strings3 = new LinkedList<>();
        for (String i : strings1) {
            for (String j : strings2) {
                strings3.add(i + j);
            }
        }

        fixedStrings.addAll(strings3);
        fixedStrings.addAll(strings2);
        fixedStrings.addAll(strings1);
        List<String> strings5 = Arrays.asList(
                "待会儿", "待会", "一会儿"
        );
        fixedStrings.addAll(strings5);

    }

    public boolean daySectionExtract(String in) {


        String pattern = "(早上|上午|早晨|中午|下午|晚上)";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {

            section = m.group(0);
            replaceInput(m, "@");

            return true;

        }
        return false;
    }

    //This function is used when daySectionExtract success;
    public boolean hourExtract(String in) {
        String pattern = "([0-9]+|[一二三四五六七八九十]+)[点时]";
        p = Pattern.compile(pattern);
        m = p.matcher(in);

        if (m.find()) {

            hour = m.group(1);
            this.in = m.replaceAll("@");

            return true;
        }
        return false;
    }

    public boolean sectionAndHourAndMinuteExtract(String in) {

        String pattern = "(早上|上午|早晨|中午|下午|晚上)([0-9]+|[一二三四五六七八九十]+)(?:[点:：])([0-9]+|[一二三四五六七八九十]+)";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {

            section = m.group(1);
            hour = m.group(2);
            minute = m.group(3);
            this.in = m.replaceAll("@");

            return true;
        }
        return false;

    }

    public boolean sectionAndHourExtract(String in) {
        String pattern = "(早上|上午|早晨|中午|下午|晚上)([0-9]+|[一二三四五六七八九十]+)(?:[点:：时])?(半)?";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {

            halfHour = m.groupCount() == 3 ? m.group(3) : null;
            section = m.group(1);
            System.out.println("sectionAndHourExtract "+section);
            hour = m.group(2);
            this.in = m.replaceAll("@");

            return true;
        }
        return false;
    }

    public boolean hourAndMinuteExtract(String in) {

        String pattern = "([0-9]+|[一二三四五六七八九十]+)(?:[点:：])([0-9]+|[一二三四五六七八九十]+|半)";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {
            hour = m.group(1);
            minute = m.group(2);
            if(minute.equals("半")){
                halfHour = "半";
                minute = null;
            }
            this.in = m.replaceAll("@");

            return true;
        }
        return false;
    }


    //When month and day is not found, try tofind day
    public boolean dayExtract(String in) {
        String pattern = "([0-9]+|[一二三四五六七八九十]+)日";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {

            day = m.group(1);
            this.in = m.replaceAll("@");

            return true;
        }
        return false;

    }

    public boolean monthAndDayExtract2(String in) {
        String pattern = "([0-9]+|[一二三四五六七八九十]+)(?:[月.\\-/])([0-9]+|[一二三四五六七八九十]+)日?";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {

            month = m.group(1);
            day = m.group(2);
            this.in = m.replaceAll("@");
            return true;
        }
        return false;

    }

    private ParseDateStringUtil parseDateStringUtil = new ParseDateStringUtil();

    public boolean valid_month_and_day(int month_in,int day_in){
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        try{
            calendar.set(Calendar.DATE,day_in);
            calendar.set(Calendar.MONTH,month_in-1);
            calendar.getTime();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean monthAndDayExtract(String in) {
        String pattern = "([0-9]+|[一二三四五六七八九十]+)(?:[月.\\-/])([0-9]+|[一二三四五六七八九十]+)日?";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        String in_2 = in;

        while(m.find()){

            month = m.group(1);
            day = m.group(2);

            Integer month_in = parseDateStringUtil.parseDay(month);
            Integer day_in = parseDateStringUtil.parseDay(day);

            if(valid_month_and_day(month_in,day_in)){
                return true;
            }
            else {
                m.replaceAll("@");
                month = null;
                day = null;
            }
        }
        this.in = in_2;
        return false;
    }

    public void tell(){
        for(String s:fixedStrings){
            System.out.println(s);
        }
    }
    public void dateExtract(String in) {
        this.in = in;
        try {

            for (String s : fixedStrings) {
                p = Pattern.compile(s);
                m = p.matcher(this.in);
                if (m.find()) {

                    this.otherHint = m.group(0);
                    break;
                }

            }

            boolean canGetSectionAndHourAndMinuteTogether = sectionAndHourAndMinuteExtract(this.in);

            if (!canGetSectionAndHourAndMinuteTogether) {
                boolean canGetHourAndMinuteTogether = hourAndMinuteExtract(this.in);
                if (!canGetHourAndMinuteTogether) {
                    boolean canGetSectionAndHourTogether = sectionAndHourExtract(this.in);
                    if (!canGetSectionAndHourTogether) {
                        hourExtract(this.in);
                    }
                }
            }
            boolean canGetMonthAndDayTogether = monthAndDayExtract(this.in);
            if (!canGetMonthAndDayTogether) {
                dayExtract(this.in);
            }

        } finally {
            System.out.println("【MONTH】    : " + month);
            System.out.println("【DAY】      : " + day);
            System.out.println("【SECTION】  : " + section);
            System.out.println("【HOUR】     : " + hour);
            System.out.println("【HALFHOUR】 : " + halfHour);
            System.out.println("【MINUTE】   : " + minute);
            System.out.println("【OTHERHINT】: " + otherHint);
        }
    }

    public HashMap<String,String> dateExtract2hash(String in) {
        this.in = in;
        try {

            for (String s : fixedStrings) {
                p = Pattern.compile(s);
                m = p.matcher(this.in);
                if (m.find()) {
                    this.otherHint = m.group(0);
                    this.in = m.replaceAll("@");
                    System.out.println(this.in);
                    break;
                }
            }



            boolean canGetSectionAndHourAndMinuteTogether = sectionAndHourAndMinuteExtract(this.in);
            if (!canGetSectionAndHourAndMinuteTogether) {

                boolean canGetHourAndMinuteTogether = hourAndMinuteExtract(this.in);
                if (!canGetHourAndMinuteTogether) {

                    boolean canGetSectionAndHourTogether = sectionAndHourExtract(this.in);
                    if (!canGetSectionAndHourTogether) {

                        hourExtract(this.in);
                    }
                }
            }
            boolean canGetMonthAndDayTogether = monthAndDayExtract(this.in);
            if (!canGetMonthAndDayTogether) {
                dayExtract(this.in);
            }

        } finally {
            System.out.println("【MONTH】    : " + month);
            System.out.println("【DAY】      : " + day);
            System.out.println("【SECTION】  : " + section);
            System.out.println("【HOUR】     : " + hour);
            System.out.println("【HALFHOUR】 : " + halfHour);
            System.out.println("【MINUTE】   : " + minute);
            System.out.println("【OTHERHINT】: " + otherHint);
            HashMap<String,String> date_detial = new HashMap<>();
            date_detial.put("month",month);
            date_detial.put("day",day);
            date_detial.put("section",section);
            date_detial.put("hour",hour);
            date_detial.put("halfhour",halfHour);
            date_detial.put("minute",minute);
            date_detial.put("otherhint",otherHint);
            return date_detial;
        }
    }

    public String dataExtract2Place(String in){
        this.in = in;
        try{
            for (String s : fixedPlaces) {
                p = Pattern.compile(s);
                m = p.matcher(this.in);
                if (m.find()) {
                    this.place = m.group(0);
                    System.out.println(this.place);
                    break;
                }
            }
        }finally {
            String data_place= "";
            data_place = this.place;
            return data_place;
        }
    }


}
