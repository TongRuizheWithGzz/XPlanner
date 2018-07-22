package com.codemover.xplanner.ConverterTest;


import com.codemover.xplanner.Converter.ScheduleitemConverter;
import com.codemover.xplanner.Model.DTO.ScheduleitmeDTO;
import com.codemover.xplanner.Model.Entity.Scheduleitme;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ScheduleitemConverterTest {

    private Scheduleitme entity;
    private ScheduleitmeDTO DTO;

    @Before
    public void init() {
        entity = new Scheduleitme();
        entity.setDescription("description");
        entity.setCompleted(false);
        entity.setAddress(null);
        entity.setStartTime(Timestamp.valueOf("2018-07-29 13:56:13"));
        entity.setEndTime(Timestamp.valueOf("2018-07-29 13:57:59"));
        entity.setTitle("title");
        entity.setImageUrl("https://www.baidu.com");


        DTO = new ScheduleitmeDTO();
        DTO.description = "description";
        DTO.completed = false;
        DTO.title = "title";
        DTO.start_time = "2018-01-29 01:02";
        DTO.end_time = "2018-02-02 13:13";
        DTO.imageUrl = null;
        DTO.address = "Shanghai Jiaotong University";
    }

    @Test
    public void canConvertEntityToDTOTest() {
        ScheduleitmeDTO scheduleitmeDTO = ScheduleitemConverter.entityToDTO(entity);
        assertThat(scheduleitmeDTO.title).isEqualTo("title");
        assertThat(scheduleitmeDTO.start_time).isEqualTo("2018-07-29 13:56");
        assertThat(scheduleitmeDTO.end_time).isEqualTo("2018-07-29 13:57");
        assertThat(scheduleitmeDTO.description).isEqualTo("description");
        assertThat(scheduleitmeDTO.imageUrl).isEqualTo("https://www.baidu.com");
        assertThat(scheduleitmeDTO.completed).isEqualTo(false);
        assertThat(scheduleitmeDTO.address).isEqualTo(null);
    }



}
