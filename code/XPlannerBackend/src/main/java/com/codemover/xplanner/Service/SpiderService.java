package com.codemover.xplanner.Service;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;

import java.util.Collection;

public interface SpiderService {

    Collection<ScheduleitmeDTO> getInfoFromWebsites(Integer pageNumber, Integer itemsPerPage);
}
