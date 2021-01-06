package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.QuestionCategoryDTO;
import eln.common.core.entities.question.QuestionCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionCategoryConverter implements IDTO<QuestionCategoryDTO>, IEntity<QuestionCategory> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public QuestionCategoryDTO convertToDTO(Object entity) {
        QuestionCategory questionCategory = (QuestionCategory) entity;
        QuestionCategoryDTO dto = modelMapper.map(questionCategory, QuestionCategoryDTO.class);
        if (questionCategory.getPoscodeVnpost() != null) {
            dto.setPcode(questionCategory.getPoscodeVnpost().getId());
        }

        return dto;
    }

    @Override
    public QuestionCategory convertToEntity(Object dto) {
        QuestionCategory entity = modelMapper.map(dto, QuestionCategory.class);
        return entity;
    }

}
