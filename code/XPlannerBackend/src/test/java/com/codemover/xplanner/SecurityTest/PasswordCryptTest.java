package com.codemover.xplanner.SecurityTest;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PasswordCryptTest {

    @Test
    public void BCryptUtilTest() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode("password");
        System.out.println(encodedPassword);
        BCryptPasswordEncoder bCryptPasswordEncoder2 = new BCryptPasswordEncoder();
        String encodedPassword2 = bCryptPasswordEncoder.encode("This_is_a_passoward");
        assertThat(encodedPassword).isNotEqualTo(encodedPassword2);
    }
}

