package com.codemover.xplanner.Threads;

import com.codemover.xplanner.DAO.NotificationRepository;
import com.codemover.xplanner.Model.Entity.Notification;
import com.codemover.xplanner.Service.Impl.Spider.ISpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Component
@Scope("prototype")
public class SpiderManager implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderManager.class);

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    @Qualifier("ElectsysSpider")
    private ISpider spider1;

    @Autowired
    @Qualifier("TongquSpider")
    private ISpider spider2;

    @Autowired
    @Qualifier("XSBSpider")
    private ISpider spider3;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... args) throws Exception {
        try {
            Integer where = 0;
            while (true) {
                CompletableFuture<Collection<Notification>> _c1 = spider1.getInfoFromWebsite(where, 1);
                CompletableFuture<Collection<Notification>> _c2 = spider2.getInfoFromWebsite(where, 1);
                CompletableFuture<Collection<Notification>> _c3 = spider3.getInfoFromWebsite(where, 1);

                CompletableFuture.allOf(_c1, _c2, _c3).join();

                Collection<Notification> c1 = _c1.get();
                Collection<Notification> c2 = _c2.get();
                Collection<Notification> c3 = _c3.get();

                if(c1.size()!=0){

                }

                where++;

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }
}
