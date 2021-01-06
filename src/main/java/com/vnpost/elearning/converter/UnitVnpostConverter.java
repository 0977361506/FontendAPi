package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.UnitVnpostDTO;
import eln.common.core.entities.unit.UnitVnpost;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UnitVnpostConverter implements IDTO<UnitVnpostDTO>, IEntity<UnitVnpost> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UnitVnpostDTO convertToDTO(Object entity) {
        UnitVnpostDTO dto = modelMapper.map(entity, UnitVnpostDTO.class);
        return dto;
    }

    @Override
    public UnitVnpost convertToEntity(Object dto) {
        UnitVnpost entity = modelMapper.map(dto, UnitVnpost.class);
        return entity;
    }
}
