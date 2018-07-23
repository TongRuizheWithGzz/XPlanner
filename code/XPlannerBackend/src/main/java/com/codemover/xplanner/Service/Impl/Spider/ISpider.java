package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Model.Entity.Notification;

import java.util.Collection;

public interface ISpider {
    Collection<Notification> getInfoFromWebsite
            (Integer offset, Integer number) throws Exception;

}
