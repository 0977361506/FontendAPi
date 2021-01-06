package com.vnpost.elearning.dto.competition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDTO extends AbstractDTO<QuestionDTO> {

    private String explain;
    private TypeQuestionDTO typeQuestion;
    private int mix;
    private String question;
    private int statusQuestion;

    @JsonIgnore
    private List<AnswerDTO> answers = new ArrayList<>();
    private LevellDTO levell;
    private TagDTO tag;
    private String createdBy;


    private List<AnswerDTO> listAnswerDTOS;
    private List<ResultDTO> resultDTOList;
    private Integer isCorrect;
    private String addOrSub;

    private String url;

    @NotNull
    private Integer type;
}
