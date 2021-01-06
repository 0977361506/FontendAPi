package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.QuestionRoundTestDTO;
import eln.common.core.entities.question.QuestionRoundTest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class QuestionRoundTestConverter implements IDTO<QuestionRoundTestDTO>, IEntity<QuestionRoundTest> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionRoundTestDTO convertToDTO(Object entity) {
        QuestionRoundTestDTO dto = modelMapper.map(entity, QuestionRoundTestDTO.class);
        return dto;
    }

    @Override
    public QuestionRoundTest convertToEntity(Object dto) {
        QuestionRoundTest entity = modelMapper.map(dto, QuestionRoundTest.class);
        return entity;
    }
}
