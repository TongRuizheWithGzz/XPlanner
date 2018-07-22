package com.codemover.xplanner.Service;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;

import java.util.Collection;

public interface SpiderService {

    Collection<ScheduleitmeDTO> getInfoFromWebsites(Integer pageNumber, Integer itemsPerPage);
}
