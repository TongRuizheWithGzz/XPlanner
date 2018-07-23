package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Model.Entity.Notification;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface ISpider {
    @Async
    CompletableFuture<Collection<Notification>> getInfoFromWebsite
            (Integer offset, Integer number) throws Exception;

}
