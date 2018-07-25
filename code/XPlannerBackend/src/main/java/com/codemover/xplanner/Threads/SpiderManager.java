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
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
@Scope("prototype")
@Order(-1)
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
    @Async
    public void run(String... args) throws Exception {
        while (true) {
            logger.info("I'm awake!");

            LinkedList<Integer> wheres = new LinkedList<>();
            wheres.add(0);
            wheres.add(0);
            wheres.add(0);
            LinkedList<ISpider> spiders = new LinkedList<>();
            spiders.add(spider1);
            spiders.add(spider2);
            spiders.add(spider3);
            try {
                while (spiders.size() != 0) {

                    ISpider spider = spiders.removeFirst();
                    Integer where = wheres.removeFirst();
                    CompletableFuture<Collection<Notification>> _c1 = spider.getInfoFromWebsite(where, 1);
                    CompletableFuture.allOf(_c1).join();
                    Collection<Notification> c1 = _c1.get();

                    if (c1.size() != 0) {
                        logger.info("size of Collection<Notification>:'{}'", c1.size());
                        Notification n = c1.iterator().next();
                        Optional<Notification> o = notificationRepository.findById(n.getNotificationId());
                        if (!o.isPresent()) {
                            logger.info("new Notification found!");
                            notificationRepository.save(n);
                            where++;
                            spiders.addLast(spider);
                            wheres.addLast(where);
                        } else {
                            logger.info("Notification up to date,will leave");
                        }
                    } else {
                        logger.info("Nothing get when get 1 per time,will continue");
                        spiders.addLast(spider);
                        wheres.addLast(where);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                logger.warn(e.getMessage());
            } finally {
                logger.info("I will sleep");
                Thread.sleep(5 * 1000 * 60);
            }
        }

    }
}