package com.codemover.xplanner.Web;

import com.codemover.xplanner.Service.ReaderService;
import com.codemover.xplanner.Web.Util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @GetMapping(value = "/api/reader")
    public Map<String, Object> reader(@RequestParam("in") String in) {
        HashMap<String, Object> response = new HashMap<>();
        String out = readerService.extractDate(in);
        Calendar calendar = Calendar.getInstance();

        response.put("start_time", out);



        return ControllerUtil.successHandler(response);
    }
}
