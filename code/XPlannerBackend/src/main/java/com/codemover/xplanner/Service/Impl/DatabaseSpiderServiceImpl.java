package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.DAO.NotificationRepository;
import com.codemover.xplanner.Model.Entity.Notification;
import com.codemover.xplanner.Service.SpiderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("DatabaseSpiderServiceImpl")
public class DatabaseSpiderServiceImpl implements SpiderService {

    @Autowired
    NotificationRepository notificationRepository;


    @Override
    public HashMap<String, Object> getInfoFromWebsites(Integer pageNumber, Integer itemsPerPage) throws Exception {
        HashMap<String, Object> notification = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(pageNumber, itemsPerPage, Sort.by("createTime").descending());

        Page<Notification> notifications = notificationRepository.findAll(pageRequest);
        notification.put("notifications", notifications.getContent());


        return notification;
    }
}
