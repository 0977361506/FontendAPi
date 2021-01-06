package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.ConditionDTO;
import eln.common.core.entities.competition.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConditionConverter implements IDTO<ConditionDTO>, IEntity<Condition> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ConditionDTO convertToDTO(Object entity) {
        ConditionDTO dto = modelMapper.map(entity, ConditionDTO.class);
        return dto;
    }

    @Override
    public Condition convertToEntity(Object dto) {
        Condition entity = modelMapper.map(dto, Condition.class);
        return entity;
    }
}
