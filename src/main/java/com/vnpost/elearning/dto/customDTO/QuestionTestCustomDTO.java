package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.competition.QuestionTestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestionTestCustomDTO extends AbstractDTO<QuestionTestCustomDTO> {

    private String namegroup;
    private Integer countQuestion;
    private String categoryName;
    private String level;
    private String typeQuestion;
    private String tag;
    List<QuestionTestDTO> questionRoundTests;



}
