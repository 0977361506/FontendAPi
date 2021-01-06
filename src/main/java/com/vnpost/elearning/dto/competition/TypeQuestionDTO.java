package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class TypeQuestionDTO extends AbstractDTO<TypeQuestionDTO> {

    private Long id;
    private Date lastUpdate;

    private String nameType;

    private Date timeCreate;
}

