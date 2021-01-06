package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AnswerDTO extends AbstractDTO<AnswerDTO> {


    private String answer;
    private Integer answerCode;
    private String contents;
    private Date lastUpdate;
    private Date timeCreate;
    private QuestionDTO question;
    private String youChose;


}
