package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.TypeQuestionDTO;
import eln.common.core.entities.question.TypeQuestion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TypeQuestionConverter implements IDTO<TypeQuestionDTO>, IEntity<TypeQuestion> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TypeQuestionDTO convertToDTO(Object entity) {
        TypeQuestionDTO dto = modelMapper.map(entity, TypeQuestionDTO.class);
        return dto;
    }

    @Override
    public TypeQuestion convertToEntity(Object dto) {
        TypeQuestion entity = modelMapper.map(dto, TypeQuestion.class);
        return entity;
    }

}
