package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Model.Entity.Notification;
import com.codemover.xplanner.Service.Impl.Spider.ISpider;
import com.codemover.xplanner.Service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service("OnFlySpiderServiceImpl")
public class OnFlySpiderServiceImpl implements SpiderService {

    @Autowired
    @Qualifier("ElectsysSpider")
    private ISpider spider1;

    @Autowired
    @Qualifier("TongquSpider")
    private ISpider spider2;

    @Autowired
    @Qualifier("XSBSpider")
    private ISpider spider3;


    @Override
    public HashMap<String, Object> getInfoFromWebsites(Integer pageNumber, Integer itemsPerPage)
            throws Exception {

        HashMap<String, Object> response = new HashMap<>();
        CompletableFuture<Collection<Notification>> _c1 = spider1.getInfoFromWebsite(pageNumber - 1, 1);
        CompletableFuture<Collection<Notification>> _c2 = spider2.getInfoFromWebsite((pageNumber - 1) * 2, 2);
        CompletableFuture<Collection<Notification>> _c3 = spider3.getInfoFromWebsite((pageNumber - 1) * 2, 2);

        CompletableFuture.allOf(_c1, _c2, _c3).join();
        Collection<Notification> c1 = _c1.get();
        Collection<Notification> c2 = _c2.get();
        Collection<Notification> c3 = _c3.get();
        c1.addAll(c2);
        c1.addAll(c2);
        response.put("notifications", c1);
        return response;
    }


}
