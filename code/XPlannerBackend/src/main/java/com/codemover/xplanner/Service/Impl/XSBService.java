package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Service.Exception.HTTPRequestNotOKException;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class XSBService {

    @Value("${url.xsb.info}")
    private String XSBUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void ParseHTMLFromXSB(Integer pageNumber)
            throws IOException {
        String url = XSBUrl + "/705-" + pageNumber.toString() + "-20.htm";

        Document doc = Jsoup.connect(url).get();
        Elements lis = doc.select("ul.list_box_5_2>li ");
        for (Element element : lis) {
            ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();

            scheduleitmeDTO.hasKnownConcretTime = false;
            Elements span = element.select("span");
            String pattern = "\\[(.*)\\]";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(span.text());
            if (m.find()) {
                scheduleitmeDTO.startTime = m.group(1) + " 00:00";
                scheduleitmeDTO.endTime = m.group(1) + " 23:59";
            }

            Element a = element.select("a").get(0);
            scheduleitmeDTO.title = a.text();
            String href = a.attr("abs:href");

            Document detailDoc = Jsoup.connect(href).get();
            Element article = detailDoc.select("div.article_box").get(0);
            String description = article.text();
            scheduleitmeDTO.description = description.substring(0, Integer.min(1023, description.length()));

            Elements imgs = article.select("img");
            if (imgs.size() == 0) {
                scheduleitmeDTO.imageUrl = null;
                continue;
            }
            String imageUrl = imgs.get(0).attr("abs:href");
            scheduleitmeDTO.imageUrl = imageUrl;

            String json = new Gson().toJson(scheduleitmeDTO);
        }

    }


}
