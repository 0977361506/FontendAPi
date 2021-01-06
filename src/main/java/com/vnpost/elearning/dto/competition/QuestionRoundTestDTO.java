package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class QuestionRoundTestDTO  extends AbstractDTO<QuestionRoundTestDTO> {

    private Date lastUpdate;
    private Date timeCreate;
    private Integer  idStructDetailTest;
    private QuestionDTO question;
    private RoundTestDTO roundTest;
    private List<AnswerDTO> listAnswerDTOS;
    private List<ResultDTO> resultDTOList;
    private Integer isCorrect;
    private String addOrSub;


}
