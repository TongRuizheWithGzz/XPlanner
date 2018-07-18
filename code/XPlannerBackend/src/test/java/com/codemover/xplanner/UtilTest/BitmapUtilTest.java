package com.codemover.xplanner.UtilTest;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BitmapUtilTest {

    @Test
    public void StringToByteTest(){
        String name="tongruizhe同睿哲";
        byte[] bytes=name.getBytes();
        System.out.println(bytes.length);
    }

}
