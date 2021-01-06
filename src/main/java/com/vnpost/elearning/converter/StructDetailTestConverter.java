package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.StructDetailTestDTO;
import eln.common.core.entities.competition.StructDetailTest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StructDetailTestConverter implements IDTO<StructDetailTestDTO>, IEntity<StructDetailTest> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StructDetailTestDTO convertToDTO(Object entity) {
        StructDetailTestDTO dto = modelMapper.map(entity, StructDetailTestDTO.class);
        return dto;
    }

    @Override
    public StructDetailTest convertToEntity(Object dto) {
        StructDetailTest entity = modelMapper.map(dto, StructDetailTest.class);
        return entity;
    }


}
