package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Model.DTO.Notification;


import com.codemover.xplanner.Service.Exception.SpiderRequestParamException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("XSBSpider")
public class XSBSpider implements ISpider {

    @Value("${url.xsb.info}")
    private String XSBUrl;

    @Value("${const.xsb.itemPerPage}")
    private Integer itemPerPage;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String website;

    public XSBSpider() {
        website = "学生办";
    }

    @Override
    public Collection<Notification> getInfoFromWebsite(Integer offset, Integer number)
            throws IOException {
        if (number > itemPerPage) {
            logger.warn("Require for too much notifications per time. Will ignore this request");
            throw new SpiderRequestParamException("Require for too much a time");
        }

        Integer startPageNumber = offset / itemPerPage;
        Integer endPageNumber = (offset + number - 1) / itemPerPage;
        Integer startIndex = offset % itemPerPage;
        Integer endIndex = (offset + number - 1) % itemPerPage;
        if (startPageNumber.equals(endPageNumber))
            return ParseHTMLFromXSB(startPageNumber + 1, startIndex, number);
        else {
            ArrayList<Notification> notifications = ParseHTMLFromXSB(startPageNumber + 1,
                    startIndex, itemPerPage - startIndex);
            ArrayList<Notification> notifications1 = ParseHTMLFromXSB(endPageNumber + 1,
                    0, endIndex + 1);
            notifications.addAll(notifications1);
            return notifications;
        }
    }


    public ArrayList<Notification> ParseHTMLFromXSB(Integer pageNumber, Integer startIndex, Integer number)
            throws IOException {
        if (startIndex + number > itemPerPage) {
            logger.warn("require too many items from XSB on a single page!");
            throw new SpiderRequestParamException("Require for too much a time");
        }

        String url = XSBUrl + "/705-" + pageNumber.toString() + "-20.htm";

        Document doc = Jsoup.connect(url).get();
        Elements lis = doc.select("ul.list_box_5_2>li ");

        ArrayList<Notification> notifications = new ArrayList<>();

        for (int index = startIndex; index - startIndex < number; index++) {
            Notification notification = new Notification();
            notification.website = website;
            Element element = lis.get(index);
            Elements span = element.select("span");
            String pattern = "\\[(.*)\\]";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(span.text());
            if (m.find()) {
                notification.start_time = m.group(1) + " 00:00";
                notification.end_time = m.group(1) + " 00:00";
            }
            Element a = element.select("a").get(0);
            notification.title = a.text();
            String href = a.attr("abs:href");
            Document detailDoc = Jsoup.connect(href).get();
            Element article = detailDoc.select("div.article_box").get(0);
            String description = article.text();
            notification.description = description.substring(0, Integer.min(1023, description.length()));
            notification.address = "";
            Elements imgs = article.select("img");
            if (imgs.size() == 0)
                notification.imageUrl = "";
            else {
                notification.imageUrl = imgs.get(0).attr("abs:src");
            }
            notifications.add(notification);
        }
        return notifications;
    }


}
