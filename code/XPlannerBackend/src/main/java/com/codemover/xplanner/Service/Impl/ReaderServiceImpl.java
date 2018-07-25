package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Service.ReaderService;
import com.codemover.xplanner.Service.Util.DateUtil;
import com.codemover.xplanner.Service.Util.ParseDateStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReaderServiceImpl implements ReaderService {


    //<--------Tools-------->
    public boolean valid_day(int day_in){
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        try{
            calendar.set(Calendar.DATE,day_in);
            calendar.getTime();
            return true;
        }catch (Exception e){
            return false;
        }
    }

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

    public String Calendar2String(Calendar calendar) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());
        return time;
    }

    public int trimHour(int hour){
        if(infectHour == true && hour <12) hour += 12;
        return hour;
    }

    private DateUtil dateUtil= new DateUtil();

    @Autowired
    private ExtractDateService extractDateService;

    private ParseDateStringUtil parseDateStringUtil = new ParseDateStringUtil();

    //<--------Variables-------->
    private Boolean infectHour = false;

    private Pattern p;
    private Matcher m;

    //<--------Variables-------->
    public void setInfectHour(Boolean infects){
        this.infectHour = infects;
    }

    //<--------Executable-------->
    public String calculateDateWithoutHint(Integer month,Integer day,Integer hour,boolean half,Integer minute,String section) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int c_year = calendar.get(Calendar.YEAR);
        int c_month = calendar.get(Calendar.MONTH);
        int c_day = calendar.get(Calendar.DATE);

        //check section
        if(section=="早上"||section=="早晨"){
            calendar.set(c_year,c_month,c_day,8,00,0);
        }
        else if(section=="上午"){
            calendar.set(c_year,c_month,c_day,10,00,0);
        }
        else if(section=="中午"){
            calendar.set(c_year,c_month,c_day,12,00,0);
        }
        else if(section =="下午"){
            calendar.set(c_year,c_month,c_day,14,00,0);
            setInfectHour(true);
        }
        else if(section == "晚上"){
            calendar.set(c_year,c_month,c_day,18,00,0);
            setInfectHour(true);
        }

        //check date
        if(day!=null){
            calendar.set(c_year,c_month,day);
            if(month!=null){
                calendar.set(c_year,month-1,day);
            }
        }

        //check hour
        if(hour!=null){
            calendar.set(Calendar.HOUR_OF_DAY,trimHour(hour));
        }

        //check half
        if(half==true){
            calendar.set(Calendar.MINUTE,30);
        }

        if(minute!=null){
            calendar.set(Calendar.MINUTE,minute);
        }

        return Calendar2String(calendar);

    }

    public String calculateDateWithHint(Integer month,Integer day,Integer hour,boolean half,Integer minute,String section,String otherhint) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int c_year = calendar.get(Calendar.YEAR);
        int c_month = calendar.get(Calendar.MONTH);
        int c_day = calendar.get(Calendar.DATE);
        String pattern = "(早上|上午|早晨|中午|下午|晚上)";
        p = Pattern.compile(pattern);
        if(otherhint!=null){
            m = p.matcher(otherhint);
            if (m.find()) {
                //find hint_section
                String hint_section = m.group(1);
                otherhint = m.replaceAll("");
                //fake_time

                if (hint_section==null){}
                else if(hint_section.equals("早上")||hint_section.equals("早晨")){
                    calendar.set(c_year,c_month,c_day,8,00,0);
                    setInfectHour(false);
                }
                else if(hint_section.equals("上午")){
                    calendar.set(c_year,c_month,c_day,10,00,0);
                    setInfectHour(false);
                }
                else if(hint_section.equals("中午")){
                    calendar.set(c_year,c_month,c_day,12,00,0);
                    setInfectHour(false);
                }
                else if(hint_section.equals("下午")){
                    calendar.set(c_year,c_month,c_day,14,00,0);
                    setInfectHour(true);
                }
                else if(hint_section.equals("晚上")){
                    calendar.set(c_year,c_month,c_day,18,00,0);
                    setInfectHour(true);
                }
            }
        }


        //可能fake会影响到正确时间
        //fake date

        HashMap<String,String> fake_date = dateUtil.default_time(otherhint);
        if(fake_date.get("errmsg").equals("200")){
            String s_time = fake_date.get("start_time");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d_date =df.parse(s_time);
            Calendar c_fake_date = Calendar.getInstance();
            c_fake_date.setTime(d_date);
            int cf_year = c_fake_date.get(Calendar.YEAR);
            int cf_month = c_fake_date.get(Calendar.MONTH);
            int cf_day = c_fake_date.get(Calendar.DATE);
            calendar.set(cf_year,cf_month,cf_day);
        }else {
            System.out.println("otherhint: 404");
        }

        //check section
        if(section==null){}
        else if(section.equals("早上")||section.equals("早晨")){
            calendar.set(Calendar.HOUR_OF_DAY,8);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            setInfectHour(false);
        }
        else if(section.equals("上午")){
            calendar.set(Calendar.HOUR_OF_DAY,10);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);            setInfectHour(false);
        }
        else if(section.equals("中午")){
            calendar.set(Calendar.HOUR_OF_DAY,12);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);            setInfectHour(false);
        }
        else if(section.equals("下午")){
            calendar.set(Calendar.HOUR_OF_DAY,14);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);            setInfectHour(true);
        }
        else if(section.equals("晚上")){
            calendar.set(Calendar.HOUR_OF_DAY,18);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);            setInfectHour(true);
        }

        //check date
        if(day!=null){
            calendar.set(c_year,c_month,day);
            if(month!=null){
                calendar.set(c_year,month-1,day);
            }
        }

        //check hour
        if(hour!=null){
            calendar.set(Calendar.HOUR_OF_DAY,trimHour(hour));
            if(minute!=null){
                calendar.set(Calendar.MINUTE,minute);
            }else{
                calendar.set(Calendar.MINUTE,0);
                System.out.println(calendar.getTime());
            }
        }

        //check half
        if(half==true){
            calendar.set(Calendar.MINUTE,30);
        }



        return Calendar2String(calendar);

    }

    public String extractDatewithParseException(String in) throws ParseException {
        HashMap<String,String> date_map = extractDateService.dateExtract2hash(in);

        String month = date_map.get("month");
        String day = date_map.get("day");
        String section = date_map.get("section");
        String hour = date_map.get("hour");
        String halfhour = date_map.get("halfhour");
        String minute = date_map.get("minute");
        String otherhint = date_map.get("otherhint");

        Boolean halfhour_in = false;
        if(halfhour==null){
            halfhour_in = false;
        }
        else if(halfhour.equals("半")){
            halfhour_in = true;
        }

        Integer hour_in = null;
        Integer minute_in = null;
        if(hour!=null){
            try{
                hour_in = parseDateStringUtil.parseDay(hour);
                if(hour_in>24)hour_in=null;
                else{
                    if(minute!=null){
                        try{
                            minute_in = parseDateStringUtil.parseDay(minute);
                            if(minute_in>60)minute_in=null;
                        }catch (NumberFormatException e){
                            //do nothing
                        }
                    }
                }
            }catch (NumberFormatException e){
                //do nothing
            }
        }

        Integer month_in = null;
        Integer day_in = null;
        if(day != null){
            try{
                day_in = parseDateStringUtil.parseDay(day);
                if(valid_day(day_in)){
                    if(month!=null){
                        try{
                            month_in = parseDateStringUtil.parseDay(month);
                            if(!valid_month_and_day(month_in,day_in)) month_in = null;
                        }catch (NumberFormatException e){
                            month_in = null;
                        }
                    }
                }
                else{
                    day_in = null;
                }
            }catch (NumberFormatException e){
                //do nothing
            }
        }

        return calculateDateWithHint(month_in,day_in,hour_in,halfhour_in,minute_in,section,otherhint);
    }

    //<--------Methods-------->
    @Override
    public String extractDate(String in){
        extractDateService.clean();
        String result = "你不可能看到我的";
        try{
            result = extractDatewithParseException(in);
        }catch (ParseException e){
            System.out.println("不可能出现的ParseException");
        }
        return result;
    }
}
