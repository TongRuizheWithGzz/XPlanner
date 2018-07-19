package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.Service.Util.ChineseTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReaderTest {

    @Before
    public void setup(){

    }

    private ChineseTool chineseTool = new ChineseTool();

    @Test
    public void Chinese2Int(){
        /*DateUtil dateUtil = new DateUtil();
        System.out.println(dateUtil.String2Int("八十七"));*/

        String hint = "十";
        if(hint.length()==2 && hint.charAt(hint.length()-2)=='十'){
            hint = "一"+hint;
        }
        int value = 0;
        int sectionNumber = 0;
        for(int i =0; i<hint.length();i++){
            int v = chineseTool.getIntMap().get(hint.charAt(i));
            if(v == 10 && !(hint.length()==1)){
                sectionNumber = v * sectionNumber;
                value = value + sectionNumber;
            }else if( i == hint.length()-1){
                value = value + v;
            }else{
                sectionNumber = v;
            }
        }
        System.out.println(value);
    }

    @Test
    public void getTime(){
        Calendar calendar = Calendar.getInstance();

        //<--------------------- get time ---------------------->
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);


        //<--------------------- reset time ---------------------->
        calendar.set(year,month,day,9,30,0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        int dayow = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(year,month,-6,9,30,0);
        String time = df.format(calendar.getTime());

        System.out.println(time);
    }
}
