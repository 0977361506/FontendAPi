package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class QuestionTestDTO  extends AbstractDTO<QuestionTestDTO> {


    private Date lastUpdate;

    private Date timeCreate;

    private Integer  idStructDetailTest;

    private QuestionDTO question;


    private TestDTO test;





    private List<AnswerDTO> listAnswerDTOS;



}
