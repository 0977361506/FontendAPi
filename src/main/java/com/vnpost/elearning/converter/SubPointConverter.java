package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.SubPointDTO;
import eln.common.core.entities.competition.SubPoint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubPointConverter implements IDTO<SubPointDTO>, IEntity<SubPoint> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubPointDTO convertToDTO(Object entity) {
        SubPointDTO dto = modelMapper.map(entity, SubPointDTO.class);
        return dto;
    }

    @Override
    public SubPoint convertToEntity(Object dto) {
        SubPoint entity = modelMapper.map(dto, SubPoint.class);
        return entity;
    }


}
