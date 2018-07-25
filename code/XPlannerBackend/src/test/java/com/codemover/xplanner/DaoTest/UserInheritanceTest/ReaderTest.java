package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.Service.ReaderService;
import com.codemover.xplanner.Service.Util.ChineseTool;
import com.codemover.xplanner.Service.Util.ParseDateStringUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReaderTest {

    @Autowired
    private ReaderService readerService;

    @Before
    public void setup() {
    }

    @Test
    public void getTimeTest() {
        Calendar calendar = Calendar.getInstance();

        //<--------------------- get time ---------------------->
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        //<--------------------- reset time ---------------------->
        calendar.set(year, month, day, 9, 30, 0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        int dayow = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(year, month, -6, 9, 30, 0);
        String time = df.format(calendar.getTime());

        assertThat(time).isEqualTo("2018-06-24 09:30");
    }

    //<--------------------------invalidDayTest0-------------------------->

    @Test(expected = NumberFormatException.class)
    public void invalidDayTest0() {
        ParseDateStringUtil.parseDay("9-8");
    }

    @Test(expected = NumberFormatException.class)
    public void invalidDayTest2() {
        ParseDateStringUtil.parseDay("八十三四");
    }

    @Test
    public void invalidDayTest3() {
        Integer result = ParseDateStringUtil.parseDay("八十三");
        assertThat(result).isEqualTo(83);
    }

    @Test(expected = NumberFormatException.class)
    public void invalidDayTest4() {
        Integer result = ParseDateStringUtil.parseDay("八百三十四");
    }

    @Test(expected = NumberFormatException.class)
    public void invalidDayTest5() {
        Integer result = ParseDateStringUtil.parseDay("三十十");
    }

    @Test
    public void invalidDayTest6() {
        Integer result = ParseDateStringUtil.parseDay("十");
        assertThat(result).isEqualTo(10);
    }

    @Test(expected = NumberFormatException.class)
    public void invalidDayTest7() {
        Integer result = ParseDateStringUtil.parseDay("其实");
    }

    @Test(expected = NumberFormatException.class)
    public void invalidDayTest8() {
        Integer result = ParseDateStringUtil.parseDay("十十");
    }

    @Test
    public void invalidDayTest9() {
        Integer result = ParseDateStringUtil.parseDay("一十二");
        assertThat(result).isEqualTo(12);
    }



    //<-----------------------------extract date test-------------------------->
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Test
    public void extractDateService0() throws ParseException {
        String s1 = readerService.extractDate("");
        Calendar calendar = Calendar.getInstance();
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService1() throws ParseException {
        String s1 = readerService.extractDate("九点");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService2() throws ParseException {
        String s1 = readerService.extractDate("9点");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService3() throws ParseException {
        String s1 = readerService.extractDate("早上");
        Calendar calendar = Calendar.getInstance();
        String s2 = simpleDateFormat.format(calendar.getTime());

        //only section is not satisfying
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService4() throws ParseException {
        String s1 = readerService.extractDate("早上九点");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        String s2 = simpleDateFormat.format(calendar.getTime());

        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService5() throws ParseException {
        String s1 = readerService.extractDate("晚上九点");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        String s2 = simpleDateFormat.format(calendar.getTime());

        assertThat(s1).isEqualTo(s2);
    }


}
