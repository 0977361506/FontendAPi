package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.StructTestDTO;
import eln.common.core.entities.competition.StructTest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class StructTestConverter implements IDTO<StructTestDTO>, IEntity<StructTest> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StructTestDTO convertToDTO(Object entity) {
        StructTestDTO dto = modelMapper.map(entity, StructTestDTO.class);
        return dto;
    }

    @Override
    public StructTest convertToEntity(Object dto) {
        StructTest entity = modelMapper.map(dto, StructTest.class);
        return entity;
    }

}
