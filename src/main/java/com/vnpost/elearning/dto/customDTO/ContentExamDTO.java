package com.vnpost.elearning.dto.customDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ContentExamDTO implements Serializable {


    private List<QuestionRTCustomDTO> questionRT;
    private Date timeStart;
    private Long idUser;

    public ContentExamDTO(List<QuestionRTCustomDTO> questionRT, Date timeStart,Long idUser) {
        this.questionRT = questionRT;
        this.timeStart = timeStart;
        this.idUser = idUser;
    }
}
