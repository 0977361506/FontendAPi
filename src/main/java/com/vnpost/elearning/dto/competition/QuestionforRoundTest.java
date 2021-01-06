package com.vnpost.elearning.dto.competition;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class QuestionforRoundTest {
    private int index;
    private QuestionDTO question;
    private List<AnswerDTO> answers;
    private List<AnswerDTO> answersS;
    private TypeQuestionDTO typeQuestion;
    private List<ResultDTO> results;

    public QuestionforRoundTest(int index, QuestionDTO question, List<AnswerDTO> answers, List<AnswerDTO> answersS,
                                TypeQuestionDTO typeQuestion) {

        this.index = index;
        this.question = question;
        this.answers = answers;
        this.answersS = answersS;
        this.typeQuestion = typeQuestion;

    }


    public QuestionforRoundTest(int index, QuestionDTO question, List<AnswerDTO> answers, List<AnswerDTO> answersS,
                                TypeQuestionDTO typeQuestion,
                                List<ResultDTO> results) {

        this.index = index;
        this.question = question;
        this.answers = answers;
        this.answersS = answersS;
        this.typeQuestion = typeQuestion;
        this.results = results;
    }





    public QuestionforRoundTest() {

    }

}
