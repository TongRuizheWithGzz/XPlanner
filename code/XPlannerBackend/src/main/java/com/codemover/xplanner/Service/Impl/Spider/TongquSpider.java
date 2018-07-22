package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Model.DTO.Notification;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Security.Exception.ParseProfileJsonException;
import com.codemover.xplanner.Service.Exception.HTTPRequestNotOKException;
import com.codemover.xplanner.Service.Util.ScheduleItemDTOFactory;
import javafx.util.Pair;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Service("TongquSpider")
public class TongquSpider implements ISpider {

    @Value("${api.tongqu.url}")
    private String tongquApiUrl;

    @Value("${api.tongqu.detail}")
    private String tongquDetailApiUrl;

    private CloseableHttpClient httpClient;

    private CloseableHttpResponse response;

    private String website;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TongquSpider() {
        httpClient = HttpClients.createDefault();
        website = "同去网";
    }


    @Override
    public Collection<Notification> getInfoFromWebsite(Integer offset, Integer number)
            throws IOException {
        if (number > 10)
            return null;
        String url = buildUrl(offset, "act.create_time");
        HttpGet httpGet = new HttpGet(url);

        response = httpClient.execute(httpGet);
        SpiderUtil.isResponseOK(response, website);

        String json = IOUtils.toString(response.getEntity().getContent());

        JSONObject jsonObject = new JSONObject(json);

        int errno = jsonObject.getInt("error");
        if (errno != 0)
            throw new HTTPRequestNotOKException("Tongqu response badly!");


        JSONObject resultJsonObject = jsonObject.getJSONObject("result");
        JSONArray acts = resultJsonObject.getJSONArray("acts");

        ArrayList<Notification> notifications = new ArrayList<>();
        for (int index = 0; index < acts.length(); index++) {

            JSONObject act = acts.getJSONObject(index);

            Notification notification = new Notification();

            notification.website = website;
            Integer actId = act.getInt("actid");
            notification.description = getDetailForAct(actId);
            notification.title = act.getString("name");
            notification.address = act.getString("location");
            notification.start_time = act.getString("start_time");
            notification.end_time = act.getString("end_time");

            try {
                notification.imageUrl = act.getString("poster");
            } catch (JSONException e) {
                notification.imageUrl = null;
                logger.info("missing poster(imageUrl) for this act:'{}'", notification.title);
            }
            notifications.add(notification);

        }
        return notifications;
    }

    private String getDetailForAct(Integer actId)
            throws IOException {
        String url = buildUrlDetail(actId);
        HttpGet httpGet = new HttpGet(url);
        response = httpClient.execute(httpGet);
        SpiderUtil.isResponseOK(response, website);
        String json = IOUtils.toString(response.getEntity().getContent());
        JSONObject detailJsonObject = new JSONObject(json);
        JSONArray jsonArray = detailJsonObject.getJSONArray("actContents");
        String description = "";
        for (int detailIndex = 0; detailIndex < jsonArray.length(); detailIndex++) {
            JSONObject detail = jsonArray.getJSONObject(detailIndex);
            boolean hasTest = detail.getBoolean("textIf");
            if (hasTest) {
                if (description.length() + detail.getString("text").length() >= 1024)
                    break;
                description += detail.getString("text");
            }
        }
        return description;
    }

    private String buildUrl(Integer offset, String orderBy) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tongquApiUrl)
                .queryParam("type", 0)
                .queryParam("status", 0)
                .queryParam("order", orderBy)
                .queryParam("offset", offset);
        return builder.toUriString();
    }


    private String buildUrlDetail(Integer actId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(tongquDetailApiUrl)
                .queryParam("id", actId);
        return builder.toUriString();
    }


}




