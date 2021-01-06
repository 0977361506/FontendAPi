package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.QuestionDTO;
import eln.common.core.entities.question.Question;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter implements IEntity<Question>, IDTO<QuestionDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionDTO convertToDTO(Object entity) {
        Question question = (Question) entity;
        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
        return questionDTO;
    }

    @Override
    public Question convertToEntity(Object dto) {
        QuestionDTO questionDTO = (QuestionDTO) dto;
        Question question = modelMapper.map(questionDTO, Question.class);
        return question;
    }
}
