package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Service.Util.ChineseTool;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExtractDateService {

    private Pattern p;
    private Matcher m;
    private List<String> fixedStrings;
    private ChineseTool chineseTool;


    private String month;
    private String day;
    private String section;
    private String hour;
    private String minute;
    private String otherHint;
    private String in;
    private String halfHour;

    public void replaceInput(Matcher m, String replacement) {
        this.in = m.replaceAll(replacement);
    }

    public ExtractDateService() {


        chineseTool = new ChineseTool();
        fixedStrings = new LinkedList<>();
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
            hour = m.group(2);
            this.in = m.replaceAll("@");

            return true;
        }
        return false;
    }

    public boolean hourAndMinuteExtract(String in) {

        String pattern = "([0-9]+|[一二三四五六七八九十]+)(?:[点:：])([0-9]+|[一二三四五六七八九十]+)";
        p = Pattern.compile(pattern);
        m = p.matcher(in);
        if (m.find()) {

            hour = m.group(1);
            minute = m.group(2);
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

    public boolean monthAndDayExtract(String in) {
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


}
