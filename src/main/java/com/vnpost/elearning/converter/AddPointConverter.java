package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.AddPointDTO;
import eln.common.core.entities.competition.AddPoint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddPointConverter implements IDTO<AddPointDTO>, IEntity<AddPoint> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddPointDTO convertToDTO(Object entity) {
        AddPointDTO dto = modelMapper.map(entity, AddPointDTO.class);
        return dto;
    }

    @Override
    public AddPoint convertToEntity(Object dto) {
        AddPoint entity = modelMapper.map(dto, AddPoint.class);
        return entity;
    }
}

