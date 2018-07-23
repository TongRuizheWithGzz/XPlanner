package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Model.Entity.Notification;
import com.codemover.xplanner.Service.Impl.Spider.ISpider;
import com.codemover.xplanner.Service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

@Service
public class SpiderServiceImpl implements SpiderService {

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
        Collection<Notification> c1 = spider1.getInfoFromWebsite(pageNumber - 1, 1);
        Collection<Notification> c2 = spider2.getInfoFromWebsite((pageNumber - 1) * 2, 2);
        Collection<Notification> c3 = spider3.getInfoFromWebsite((pageNumber - 1) * 2, 2);
        c1.addAll(c2);
        c1.addAll(c2);
        response.put("notifications", c1);
        return response;
    }


}
