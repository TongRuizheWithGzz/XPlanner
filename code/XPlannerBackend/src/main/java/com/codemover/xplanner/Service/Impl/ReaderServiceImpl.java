package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Service.ReaderService;
import com.codemover.xplanner.Service.Util.DateUtil;
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
    @Override
    public HashMap<String, String> extractDate(String in) throws ParseException {
        System.out.println(calculateDateWithHint(null,null,3,true,1,null,"周六下午"));
        return null;
    }

    //<--------Tools-------->
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
        m = p.matcher(otherhint);

        if (m.find()) {
            //find hint_section
            String hint_section = m.group(1);
            otherhint = m.replaceAll("");
            //fake_time
            if(hint_section.equals("早上")||hint_section.equals("早晨")){
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

        //可能fake会影响到正确时间
        //fake date
        HashMap<String,String> fake_date = dateUtil.default_time(otherhint);
        System.out.println("asd");
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
            System.out.println("hint: 404");
        }

        //check section
        if(section.equals("早上")||section.equals("早晨")){
            calendar.set(c_year,c_month,c_day,8,00,0);
            setInfectHour(false);
        }
        else if(section.equals("上午")){
            calendar.set(c_year,c_month,c_day,10,00,0);
            setInfectHour(false);
        }
        else if(section.equals("中午")){
            calendar.set(c_year,c_month,c_day,12,00,0);
            setInfectHour(false);
        }
        else if(section.equals("下午")){
            calendar.set(c_year,c_month,c_day,14,00,0);
            setInfectHour(true);
        }
        else if(section.equals("晚上")){
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

}
