package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import eln.common.core.entities.unit.PoscodeVnpost;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PoscodeVnpostConverter implements IDTO<PoscodeVnpostDTO>, IEntity<PoscodeVnpost> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PoscodeVnpostDTO convertToDTO(Object entity) {
        PoscodeVnpostDTO dto = modelMapper.map(entity, PoscodeVnpostDTO.class);
        return dto;
    }

    @Override
    public PoscodeVnpost convertToEntity(Object dto) {
        PoscodeVnpost entity = modelMapper.map(dto, PoscodeVnpost.class);
        return entity;
    }
}
