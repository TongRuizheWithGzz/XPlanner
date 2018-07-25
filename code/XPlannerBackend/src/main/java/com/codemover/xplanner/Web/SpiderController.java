package com.codemover.xplanner.Web;

import com.codemover.xplanner.Service.SpiderService;
import com.codemover.xplanner.Web.Util.ControllerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
public class SpiderController {

    @Autowired
    @Qualifier("DatabaseSpiderServiceImpl")
    SpiderService spiderService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping(value = "/api/notifications")
    Map<String, Object> getNotificationsFromWebsite(@RequestParam("pageNumber") Integer pageNumber,
                                                    @RequestParam("size") Integer size) throws Exception {
//        logger.info("User '{} is using spider'",p.getName());
        return ControllerUtil.successHandler(spiderService.getInfoFromWebsites(pageNumber, size));

    }


}
