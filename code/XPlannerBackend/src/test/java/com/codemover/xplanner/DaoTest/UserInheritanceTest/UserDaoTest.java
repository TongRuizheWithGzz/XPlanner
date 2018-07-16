package com.codemover.xplanner.DaoTest.UserInheritanceTest;

import com.codemover.xplanner.DAO.JAccountUserRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.DAO.WeixinUserRepository;
import com.codemover.xplanner.Model.Entity.JAccountUser;
import com.codemover.xplanner.Model.Entity.Role;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Model.Entity.WeixinUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDaoTest {


    @Autowired
    private JAccountUserRepository jAccountUserRepository;

    @Autowired
    private WeixinUserRepository weixinUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        JAccountUser jAccountUser = new JAccountUser();
        jAccountUser.setUserName("F18A4E9E-B435-4D2C-8679-A5CD2D285655");
        jAccountUser.setUserPassword("ok");
        jAccountUser.setClassNumber("F1603702");
        jAccountUser.setUniqueId("F18A4E9E-B435-4D2C-8679-A5CD2D285655");
        jAccountUser.setRealName("同睿哲");
        jAccountUser.setStudentId("516030910193");
        jAccountUser.setjAccountName("tongruizhe");

        Role role = new Role("NORMAL", "ROLE_JACCOUNT_USER");
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);

        jAccountUser.setRoles(roles);
        jAccountUserRepository.save(jAccountUser);
    }

    @Test
    public void insertIntoSubclassAndSuperClassTableTest() {
        JAccountUser getUserFromJAccount =
                jAccountUserRepository.findByUserName("F18A4E9E-B435-4D2C-8679-A5CD2D285655");
        User getUserFromSuperClass =
                userRepository.findByUserName("F18A4E9E-B435-4D2C-8679-A5CD2D285655");
        assertThat(getUserFromJAccount.getRealName()).isEqualTo("同睿哲");
        assertThat(getUserFromSuperClass.getUserName()).isEqualTo("F18A4E9E-B435-4D2C-8679-A5CD2D285655");
        Set<Role> roles = getUserFromJAccount.getRoles();
        assertThat(roles.size()).isEqualTo(1);
        for (Role role : roles) {
            assertThat(role.getRolename()).isEqualTo("ROLE_JACCOUNT_USER");
            assertThat(role.getStatus()).isEqualTo("NORMAL");
        }
    }

    @Test
    public void onlyInsertIntoSpecificSubclassTest() {
        WeixinUser weixinUser = weixinUserRepository.findByUserName("F18A4E9E-B435-4D2C-8679-A5CD2D285655");
        assertThat(weixinUser).isEqualTo(null);
    }


}
