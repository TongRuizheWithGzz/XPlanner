package com.codemover.xplanner.Service.Impl;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ElectsysService {
    @Value("${url.electsys.info}")
    private String EleUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void getInfoFromElectsys() throws IOException {

        ScheduleitmeDTO scheduleitmeDTO = new ScheduleitmeDTO();
        Document doc = Jsoup.connect(EleUrl).headers(forgeHeaders()).get();
        Elements TDs = doc.select("TD.18line[colspan=2]");
        for (Element td : TDs) {

            Element font = td.select("font.date").get(0);
            String pattern = "//((.+)//)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(font.text());
            if (m.find())
                System.out.println(m.group(0));
            System.out.println(m.group(1));
        }
    }

    public HashMap<String, String> forgeHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headers.put("Cache-Control", "no-cache");
        headers.put("Pragma", "no-cache");
        headers.put("Proxy-Connection", "keep-alive");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("Host", "electsys.sjtu.edu.cn");
        return headers;
    }
}
