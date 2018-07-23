package com.codemover.xplanner.DaoTest.UserInheritanceTest.UserInheritanceTest;

import com.codemover.xplanner.DAO.NotificationRepository;
import com.codemover.xplanner.Model.Entity.Notification;
import com.codemover.xplanner.Service.Impl.Spider.SpiderUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NotificationDaoTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Before
    public void setUp() {
        Notification notification = new Notification();
        notification.setEnd_time("2016-08-08 09:32");
        notification.setStart_time("2016-08-08 09:32");
        notification.setNotificationId(notification.hashCode());
        notificationRepository.save(notification);

    }

    @Test
    public void canGetSetUpOnes() {
        Notification tmpN = new Notification();
        tmpN.setEnd_time("2016-08-08 09:32");
        tmpN.setStart_time("2016-08-08 09:32");
        Optional<Notification> notification = notificationRepository.findById(tmpN.hashCode());
        assertThat(notification.isPresent()).isEqualTo(true);

        Notification n = notification.get();
        assertThat(n.getNotificationId()).
                isEqualTo(SpiderUtil.hashCode("2016-08-08 09:32" + "2016-08-08 09:32"));
        System.out.println(n.getNotificationId());
        System.out.println(n.getStart_time());
        System.out.println(n.getCreate_time());
    }
}
