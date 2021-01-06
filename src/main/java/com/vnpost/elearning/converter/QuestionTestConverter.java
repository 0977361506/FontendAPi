package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.QuestionTestDTO;
import eln.common.core.entities.question.QuestionTest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionTestConverter implements IDTO<QuestionTestDTO>, IEntity<QuestionTest> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionTestDTO convertToDTO(Object entity) {
        QuestionTestDTO dto = modelMapper.map(entity, QuestionTestDTO.class);
        return dto;
    }

    @Override
    public QuestionTest convertToEntity(Object dto) {
        QuestionTest entity = modelMapper.map(dto, QuestionTest.class);
        return entity;
    }


}
