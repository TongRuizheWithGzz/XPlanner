package com.codemover.xplanner.Service;

import java.util.HashMap;

public interface SpiderService {

    HashMap<String ,Object> getInfoFromWebsites(Integer pageNumber, Integer itemsPerPage) throws Exception;
}
