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
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
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

    @Test
    public void extractDateService6() throws ParseException {
        String s1 = readerService.extractDate("明天晚上九点");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        String s2 = simpleDateFormat.format(calendar.getTime());

        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService7() throws ParseException {
        String s1 = readerService.extractDate("本周六9点");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = calendar.get(Calendar.DATE);
        int this_Monday = day-(day_of_week-2);
        int that_day = this_Monday + (6-1);
        calendar.set(year,month,that_day,9,00,0);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }
/*

    @Test
    public void extractDateService7_1() throws ParseException {
        String s1 = readerService.extractDate("本周六九点");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = calendar.get(Calendar.DATE);
        int this_Monday = day-(day_of_week-2);
        int that_day = this_Monday + (6-1);
        calendar.set(year,month,that_day,9,00,0);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }
*/

    @Test
    public void extractDateService8() throws ParseException {
        String s1 = readerService.extractDate("本周六9点13分");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int day = calendar.get(Calendar.DATE);
        int this_Monday = day-(day_of_week-2);
        int that_day = this_Monday + (6-1);
        calendar.set(year,month,that_day,9,13,0);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService9() throws ParseException {
        String s1 = readerService.extractDate("9月20日9点13分");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.DATE,20);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,13);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService10() throws ParseException {
        String s1 = readerService.extractDate("九月二十日九点十三分");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.DATE,20);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,13);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService11() throws ParseException {
        String s1 = readerService.extractDate("9-20九点十三分");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.DATE,20);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,13);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService12() throws ParseException {
        String s1 = readerService.extractDate("9-20 9：13");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.DATE,20);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,13);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

/*    @Test
    public void extractDateService13_1() throws ParseException {
        String s1 = readerService.extractDate("9-20 九点半");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,8);
        calendar.set(Calendar.DATE,20);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,30);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService13_2() throws ParseException {
        String s1 = readerService.extractDate("明天九点半");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,30);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService13_3() throws ParseException {
        String s1 = readerService.extractDate("九点半");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,30);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }*/

    @Test
    public void extractDateService14() throws ParseException {
        String s1 = readerService.extractDate("上午九点半");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,30);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService15() throws ParseException {
        String s1 = readerService.extractDate("明天上午九点半");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,30);
        String s2 = simpleDateFormat.format(calendar.getTime());
        assertThat(s1).isEqualTo(s2);
    }

    @Test
    public void extractDateService16() throws ParseException {
        String s1 = readerService.extractDate("2018年7月15日上午9:13分");
        assertThat(s1).isEqualTo("2018-07-15 09:13");
    }

    @Test
    public void extractDateService17() throws ParseException {
        String s1 = readerService.extractDate("2018年7月15日9:13分");
        assertThat(s1).isEqualTo("2018-07-15 09:13");
    }

    @Test
    public void extractDateService18() throws ParseException {
        String s1 = readerService.extractDate("【紧急通知】请各位上海户籍的同学（高考时是上海户口），" +
                "周四（24日）中午12:00之前把兵役登记证的复印件交到电群3-102办公室助管那里，下午上海市过来检查");
        assertThat(s1).isEqualTo("2018-07-26 12:00");
    }

    @Test
    public void extractDateService19() throws ParseException {
        String s1 = readerService.extractDate("大家中午好~ 请各位和上一届部长团成员交接好【学" +
                "活办公室钥匙】，并且麻烦大家在【今天20:00】" +
                "以前报给我钥匙的状态，包括【损坏】【遗漏】或者【正常】，" +
                "秘书处会统计并配好钥匙，在第一次例会上给大家~ 祝还没考完试的同学考试顺利噢！");
        assertThat(s1).isEqualTo("2018-07-26 20:00");
    }

    @Test
    public void extractDateService20() throws ParseException {
        String s1 = readerService.extractDate("今年第10号台风“安比”正在逼近我国华东沿海地区。18日“安比”生成，" +
                "19日9时，上海市气象局已启动台风四级应急响应，" +
                "上海将出现严重的暴风雨天气。请各位学联er注意人身安全，平安度过假期。\n");
        assertThat(s1).isEqualTo("2018-07-18 09:00");
    }

    @Test
    public void extractDateService21() throws ParseException {
        String s1 = readerService.extractDate("陈诺 发表于 04-25 13:31");
        assertThat(s1).isEqualTo("2018-04-25 13:31");
    }

    @Test
    public void extractDateService22() throws ParseException {
        String s1 = readerService.extractDate("4.25下午1点15在软院演播厅开年级会");
        assertThat(s1).isEqualTo("2018-04-25 13:15");
    }

    @Test
    public void extractDateService23() throws ParseException {
        String s1 = readerService.extractDate("【通知】各位同学好！现定于4月25日（周三）下午13:15-15:30，" +
                "开大二年级会，内容主要关于专业方向介绍，请大家相互转告，准时参加~");
        assertThat(s1).isEqualTo("2018-04-25 13:15");
    }

    @Test
    public void extractDateService24() throws ParseException {
        String s1 = readerService.extractDate("明天（4月9日周一）13：00开始小学期第二轮选课哦~安排在夏季学期19周及以后的执行计划内课程诸如军训、各类实践实习等必修限选类课程，将在夏季学期第二轮抢选及第三轮选课阶段开放，" +
                "请同学务必在此选课期间选上执行计划内必修及限选课程，" +
                "选课系统关闭后将不再受理补选补退申请。具体选课流程为：");
        assertThat(s1).isEqualTo("2018-04-09 13:00");
    }

    @Test
    public void extractDateService25() throws ParseException {
        String s1 = readerService.extractDate("4月9日周一下午一点开始小学期第二轮选课哦~安排在夏季学期19周及以后的执行计划内课程诸如军训、各类实践实习等必修限选类课程，将在夏季学期第二轮抢选及第三轮选课阶段开放，" +
                "请同学务必在此选课期间选上执行计划内必修及限选课程，" +
                "选课系统关闭后将不再受理补选补退申请。具体选课流程为：");
        assertThat(s1).isEqualTo("2018-04-09 13:00");
    }

    @Test
    public void extractDateService26() throws ParseException {
        String s1 = readerService.extractDate("海选：3月28日（第5周周三）13:00—4月3日（第6周周二）8:30；\n");
        assertThat(s1).isEqualTo("2018-03-28 13:00");
    }

    @Test
    public void extractDateService27() throws ParseException {
        String s1 = readerService.extractDate("海选：本周三 13:00—下周二 8:30；\n");
        assertThat(s1).isEqualTo("2018-07-25 13:00");
    }

    @Test
    public void extractDateService28() throws ParseException {
        String s1 = readerService.extractDate("观念比能力重要，策划比实施重要，行动比承诺重要，选择比努力重要，感知比告知重要，创造比证明重要，尊重生命比别人看法重要！\n" +
                "周六下午15：07-16：00体验课：夏冬秋 -  嗨！你会用PS制作音乐视频相册吗？");
        assertThat(s1).isEqualTo("2018-07-28 15:07");
    }
}
