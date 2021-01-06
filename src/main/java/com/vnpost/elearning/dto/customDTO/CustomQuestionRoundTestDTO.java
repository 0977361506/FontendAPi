package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.competition.QuestionRoundTestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomQuestionRoundTestDTO extends AbstractDTO<CustomQuestionRoundTestDTO> {
    private String namegroup;
    private Integer countQuestion;
    private String categoryName;
    private String level;
    private String typeQuestion;
    private String tag;
    List<QuestionRoundTestDTO> questionRoundTests;
    private List<QuestionRoundTestDTO>  questionNull;

}
