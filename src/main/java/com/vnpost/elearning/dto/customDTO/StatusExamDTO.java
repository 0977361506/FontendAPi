package com.vnpost.elearning.dto.customDTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StatusExamDTO implements Serializable {
    private Long idRound;
    private Integer statusExam;
    private Integer point;
    private Float persent;

    public StatusExamDTO(Long idRound, Integer statusExam, Integer point,Float persent) {
        this.idRound = idRound;
        this.statusExam = statusExam;
        this.point = point;
        this.persent = persent;
    }
    public StatusExamDTO(){

    }
}
