package com.codemover.xplanner.Service.Impl.Spider;

import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Collection;

public interface ISpider {
    Collection<Pair<ScheduleitmeDTO, String>> getInfoFromWebsite
            (Integer offset, Integer number) throws IOException;

}
