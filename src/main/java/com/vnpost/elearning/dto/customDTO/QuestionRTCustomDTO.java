package com.vnpost.elearning.dto.customDTO;



import com.vnpost.elearning.dto.competition.AnswerDTO;
import com.vnpost.elearning.dto.competition.QuestionDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class QuestionRTCustomDTO  implements Serializable {
    private int index;
    private QuestionDTO question;
    private List<AnswerDTO> answers;
    private Long typeQuestion;

    public QuestionRTCustomDTO(int index, QuestionDTO question, List<AnswerDTO> answers, Long typeQuestion) {
        this.index = index;
        this.question = question;
        this.answers = answers;
        this.typeQuestion = typeQuestion;
    }
}
