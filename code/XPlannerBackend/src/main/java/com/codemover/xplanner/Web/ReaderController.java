package com.codemover.xplanner.Web;

import com.codemover.xplanner.Service.ReaderService;
import com.codemover.xplanner.Web.Util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping(value = "/api/reader")
    public Map<String, Object> reader(@RequestParam("in") String in) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            String out = readerService.extractDate(in);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(out);
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);

            calendar.add(Calendar.HOUR_OF_DAY, 1);
            String time2 = sdf.format(calendar.getTime());

            response.put("start_time", out);
            response.put("end_time", time2);
            return ControllerUtil.successHandler(response);
        } catch (ParseException e) {
            return null;
        }
    }
}
