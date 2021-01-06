package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionCompetitonService {
    @Autowired
    CompetitionConverter competitionConverter ;
    @Autowired
    CompetitionCategoryConverter competitionCategoryConverter;
    @Autowired
    AnswerConverter answerConverter ;
    @Autowired
    QuestionConverter questionConverter ;
    @Autowired
    TypeQuestionConverter typeQuestionConverter ;
    @Autowired
    RoundTestService r;
    @Autowired
    CandidateService can;




}
