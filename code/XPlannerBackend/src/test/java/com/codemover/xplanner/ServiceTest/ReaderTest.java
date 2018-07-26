package com.codemover.xplanner.ServiceTest;

import com.codemover.xplanner.Service.Impl.ExtractDateService;
import com.codemover.xplanner.Service.ReaderService;
import com.codemover.xplanner.Service.Util.DateUtil;
import com.codemover.xplanner.Service.Util.ParseDateStringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class ReaderTest {
    private ExtractDateService extractDateService;

    public ReaderTest() {
        extractDateService = new ExtractDateService();
    }

    @Test
    public void a() throws ParseException {
        System.out.println(String.valueOf(3));

    }


    @Test
    public void canExtractManyDateFormatTest1() {
        extractDateService.dateExtract("2018年7月15日上午9:13分");
    }

    @Test
    public void canExtractManyDateFormatTest2() {
        extractDateService.dateExtract("2018年7月15日9:13分");
    }

    @Test
    public void canExtractManyDateFormatTest3() {
        //Found
        extractDateService.dateExtract("qefijadsf惊世毒妃8.13皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃08.13皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃8.5皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃8.05皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃08.03皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃8月13日皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃8月13日皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃8月七日皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃十二月13日皆无法");
        extractDateService.dateExtract("qefijadsf惊世毒妃八月十三日皆无法");

    }

    @Test
    public void test() {
        extractDateService.dateExtract("【紧急通知】请各位上海户籍的同学（高考时是上海户口），" +
                "周四（24日）中午12:00之前把兵役登记证的复印件交到电群3-102办公室助管那里，下午上海市过来检查");
    }

    @Test
    public void test2() {
        extractDateService.dateExtract("大家中午好~ 请各位和上一届部长团成员交接好【学" +
                "活办公室钥匙】，并且麻烦大家在【今天20:00】" +
                "以前报给我钥匙的状态，包括【损坏】【遗漏】或者【正常】，" +
                "秘书处会统计并配好钥匙，在第一次例会上给大家~ 祝还没考完试的同学考试顺利噢！");
    }

    @Test
    public void test3() {
        extractDateService.dateExtract("今年第10号台风“安比”正在逼近我国华东沿海地区。18日“安比”生成，" +
                "19日9时，上海市气象局已启动台风四级应急响应，" +
                "上海将出现严重的暴风雨天气。请各位学联er注意人身安全，平安度过假期。\n");
    }

    @Test
    public void test4() {
        extractDateService.dateExtract("陈诺 发表于 04-25 13:31");
    }

    @Test
    public void test5() {
        extractDateService.dateExtract("4.25下午1点15在软院演播厅开年级会");
    }

    @Test
    public void test6() {
        extractDateService.dateExtract("【通知】各位同学好！现定于4月25日（周三）下午13:15-15:30，" +
                "开大二年级会，内容主要关于专业方向介绍，请大家相互转告，准时参加~");
    }

    @Test
    public void test7() {
        extractDateService.dateExtract("明天（4月9日周一）13：00开始小学期第二轮选课哦~安排在夏季学期19周及以后的执行计划内课程诸如军训、各类实践实习等必修限选类课程，将在夏季学期第二轮抢选及第三轮选课阶段开放，" +
                "请同学务必在此选课期间选上执行计划内必修及限选课程，" +
                "选课系统关闭后将不再受理补选补退申请。具体选课流程为：");
    }

    @Test
    public void test7_1() {
        extractDateService.dateExtract("4月9日周一下午一点开始小学期第二轮选课哦~安排在夏季学期19周及以后的执行计划内课程诸如军训、各类实践实习等必修限选类课程，将在夏季学期第二轮抢选及第三轮选课阶段开放，" +
                "请同学务必在此选课期间选上执行计划内必修及限选课程，" +
                "选课系统关闭后将不再受理补选补退申请。具体选课流程为：");
    }

    @Test
    public void test8() {
        extractDateService.dateExtract("2017—2018年度上海交通大学电子信息与电气工程学院五四评优工作正在开展，" +
                "请仔细阅读相关文件，希望大家积极参评。\n" +
                "注意:第一阶段院级申报在金数据https://jinshuju.net/f/nil19v上自主进行，截止日期为4月7日。");
    }

    @Test
    public void test9_1() {
        extractDateService.dateExtract("海选：3月28日（第5周周三）13:00—4月3日（第6周周二）8:30；\n");
    }

    @Test
    public void test9_2() {
        extractDateService.dateExtract2hash("海选：本周三 13:00—下周二 8:30；\n");
    }

    @Test
    public void test10() {
        extractDateService.dateExtract("建模，程序作业已分配完毕，请各位同学及时查看，提交" +
                "时间有8.5，8.15，8.25，请各位同学按照指定时间提交作业。同时每组");
    }

    @Test
    public void test11() {
        extractDateService.dateExtract("观念比能力重要，策划比实施重要，行动比承诺重要，选择比努力重要，感知比告知重要，创造比证明重要，尊重生命比别人看法重要！\n" +
                "周六下午15：07-16：00体验课：夏冬秋 -  嗨！你会用PS制作音乐视频相册吗？");
    }

    @Test
    public void test12() {

        extractDateService.dateExtract("观念比能力重要，策划比实施重要，行动比承诺重要，选择比努力重要，感知比告知重要，创造比证明重要，尊重生命比别人看法重要！\n" +
                "下午一点半 体验课：夏冬秋 -  嗨！你会用PS制作音乐视频相册吗？");
    }

    @Test
    public void test13() {
        extractDateService.dateExtract("观念比能力重要，策划比实施重要，行动比承诺重要，选择比努力重要，感知比告知重要，创造比证明重要，尊重生命比别人看法重要！\n" +
                "下午一点 体验课：夏冬秋 -  嗨！你会用PS制作音乐视频相册吗？");
    }

    @Test
    public void tellMe() {
        extractDateService.tell();
    }

}
