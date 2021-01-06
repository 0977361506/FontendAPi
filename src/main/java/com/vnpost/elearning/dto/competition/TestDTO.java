package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TestDTO extends AbstractDTO<TestDTO> {

    private Date lastUpdate;

    private String name;

    private Date timeCreate;

    private Integer typeTest;
    private String description;
    private StructTestDTO structTest;

    private TestkitDTO testKit;

    private UsersDTO user;


    private String id_test_kit;
    private String[] listStructTest;
    Integer countQuestion;
    private String id_structTest;

}
