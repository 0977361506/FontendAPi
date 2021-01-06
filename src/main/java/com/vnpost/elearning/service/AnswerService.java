package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.AnswerConverter;
import com.vnpost.elearning.dto.competition.AnswerDTO;
import eln.common.core.entities.question.Answer;
import eln.common.core.repository.AnswerRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerConverter answerConverter;


    public List<Answer> findByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    public Answer findAnswerByidQuesAndAns(String aString, Long id) {
        String hString = "from Answer a where a.question.id=:id and a.answer=:ans";
        List<Answer> list = entityManager.unwrap(Session.class).createQuery(hString).setParameter("id", id).setParameter("ans", aString).getResultList();
        if (list.size() > 0) return list.get(0);
        return null;


    }


    public List<AnswerDTO> findByIdQuestion(Long id_question) {
        List<Answer> answerList = answerRepository.findByIdQuestion(id_question + "");
        List<AnswerDTO> answerDTOList = new ArrayList<>();
        for (Answer answer : answerList) {
            answerDTOList.add(answerConverter.convertToDTO(answer));
        }
        return answerDTOList;
    }
}
