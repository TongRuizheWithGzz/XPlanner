package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;

import javax.print.Doc;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ElectsysService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${url.electsys.info}")
    private String EleUrl;


    @Value("${url.electsys.login}")
    private String EleLoginUrl;

    private CloseableHttpClient httpClient;

    private CloseableHttpResponse response;

    public ElectsysService() {
        httpClient = HttpClients.createDefault();


    }

    public String getIndexPage() throws IOException {
        HttpGet httpGetIndexPage = new HttpGet(EleUrl);

        logger.info("Visit electsys index page");
        response = httpClient.execute(httpGetIndexPage);


        int statusCode = response.getStatusLine().getStatusCode();
        logger.info("Get status code from electsys: '{}'", statusCode);

        return IOUtils.toString(response.getEntity().getContent());

    }


    public List<ScheduleitmeDTO> getInfoFromElectsys() throws IOException {
        ArrayList<ScheduleitmeDTO> scheduleitmeDTOS = new ArrayList<>();
        String html = getIndexPage();
        Document doc = Jsoup.parse(html);
        Elements TDs = doc.select("TD.18line");

        for (Element td : TDs) {
            ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();
            Element font = td.select("font.date").get(0);
            Element href = td.select("a").get(0);
            String title = href.text();
            String detailUrl = href.attr("abs:href");
            scheduleitmeDTO.title = title;


            String pattern = "\\((?:([0-9]{4})年([0-9]{1,2})月([0-9]{1,2})日)\\)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(font.text());
            if (m.find()) {
                String year = m.group(1);
                String month = m.group(2).length() == 1 ? "0" + m.group(2) : m.group(2);
                String day = m.group(3).length() == 1 ? "0" + m.group(3) : m.group(3);

                String formatDateString = year + "-" + month + "-" + day + " 00:00";
                scheduleitmeDTO.start_time = formatDateString;
                scheduleitmeDTO.end_time = formatDateString;
            }

            HttpGet httpGet = new HttpGet(detailUrl);
            response = httpClient.execute(httpGet);
            String detailHtml = IOUtils.toString(response.getEntity().getContent(), "gb2312");
            Document detailDoc = Jsoup.parse(detailHtml);

            try {
                Element table = detailDoc.select("table.main_r_co_fo").get(0);
                scheduleitmeDTO.description = table.text().substring(0, Integer.min(1024, table.text().length()));
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            scheduleitmeDTOS.add(scheduleitmeDTO);
        }
        String responseString=new Gson().toJson(scheduleitmeDTOS);
        return scheduleitmeDTOS;
    }


}
