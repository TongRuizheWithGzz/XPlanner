package com.codemover.xplanner.UtilTest;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.google.common.hash.Hashing;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class HashTest {

    @Test
    public void sameStringGetSameCode() {
        String input1 = "项目进入后期了，大家要注重测试，除了功能测试，还要做兼容性测试（例如不同浏览器，不同分辨率，不同操作系统等）、以及性能测试等。性能测试要用自动化性能测试工具，模拟多个用户进" +
                "行测试。系统测试要有《测试用" +
                "例》和《测试报告》文档。如果进行了单元测试" +
                "，写了单元测试代码，则额外加分。";
        String input2 = "项目进入后期了，大家要注重测试，除了功能测试，还要做兼容性测试（例如不同浏览器，不同分辨率，不同操作系统等）、以及性能测试等。性能测试要用自动化性能测试工具，模拟多个用户进" +
                "行测试。系统测试要有《测试用" +
                "例》和《测试报告》文档。如果进行了单元测试" +
                "，写了单元测试代码，则额外加分。";
        Integer output1 = Hashing.
                murmur3_32().
                hashBytes(input1.getBytes())
                .asInt();
        Integer output2 = Hashing.
                murmur3_32().
                hashBytes(input2.getBytes())
                .asInt();
        assertThat(output1).isEqualTo(output2);
    }


    @Test
    public void differentStringGetDifferentCode() {
        String input1 = "项目进入后期了，大家要注重测试，除了功能测试，还要做兼容性测试（例如不同浏览器，不同分辨率，不同操作系统等）、以及性能测试等。性能测试要用自动化性能测试工具，模拟多个用户进" +
                "行测试。系统测试要有《测试用" +
                "例》和《测试报告》文档。如果进行了单元测试" +
                "，写了单元测试代码，则额外加分。";
        String input2 = "a";
        Integer output1 = Hashing.
                murmur3_32().
                hashBytes(input1.getBytes())
                .asInt();
        Integer output2 = Hashing.
                murmur3_32().
                hashBytes(input2.getBytes())
                .asInt();
        assertThat(output1).isNotEqualTo(output2);
    }


}
