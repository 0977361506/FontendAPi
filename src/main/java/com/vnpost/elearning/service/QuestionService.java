package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.QuestionConverter;
import eln.common.core.entities.question.Question;
import eln.common.core.repository.question.QuestionRepository;
import eln.common.core.repository.QuestionRoundTestRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionConverter converter;
    @Autowired
    private ResultService resultService;
    @Autowired
    private TypeQuestionService typeQuestionService;
    @Autowired
    private LevellService levellService;
    @Autowired
    private QuestionCategoryService questionCategoryService;
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionRoundTestRepository questionRoundTestRepository;


    public Question findbyId(Long id) {
        return entityManager.unwrap(Session.class).find(Question.class, id);
    }


    public List<Question> findAll() {
        return questionRepository.findAll();
    }


    public Question findById(Long id) {
        return questionRepository.findById(id).get();
    }





    public Integer saveQuestion(String id_struct_detail, String id_add_round, String id_add_question) {
        try {
            Integer count = questionRoundTestRepository.countByidQuestionIdRound(id_add_question, id_add_round);
            if (count <= 0) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time_create_time_update = dateFormat.format(java.util.Calendar.getInstance().getTime());
                questionRepository.saveQuestionForRoundTest(id_add_question, id_add_round, id_struct_detail, time_create_time_update, time_create_time_update);
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            return 1;
        }

    }


}
