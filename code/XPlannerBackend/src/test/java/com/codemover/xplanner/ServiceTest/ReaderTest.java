package com.codemover.xplanner.ServiceTest;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class ReaderTest {

    @Test
    public void canExtractManyDateFormatTest() {
        String string1 = "(123)456-7890";

        String pattern1="([0-9]+|[一二三四五六七八九十]+)(?:[月.-])([0-9]+|[一二三四五六七八九十]+)日?";

        Integer.parseInt("2389");
        Pattern p1 = Pattern.compile(pattern1);
        Matcher m1 = p1.matcher(string1);
        if (m1.find())
            System.out.println(m1.group(1));
    }
}