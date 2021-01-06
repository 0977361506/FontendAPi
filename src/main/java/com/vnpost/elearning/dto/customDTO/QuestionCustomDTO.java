package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.competition.QuestionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionCustomDTO extends AbstractDTO<QuestionCustomDTO> {
    List<QuestionDTO> questionDTOList;
    Integer count;

}
