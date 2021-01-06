package com.vnpost.elearning.dto.competition;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class TimeStartExamDTO implements Serializable {

    private Long id;
    private Integer countTest;
    private Date timeStart;
    private Long idRoundTest;
    private Long idUser;


}
