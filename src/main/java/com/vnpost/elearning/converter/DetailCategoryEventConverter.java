package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.DetailCategoryEventDTO;
import eln.common.core.entities.document.DetailCategoryEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetailCategoryEventConverter   implements IDTO<DetailCategoryEventDTO>, IEntity<DetailCategoryEvent>{
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DetailCategoryEventDTO convertToDTO(Object entity) {
        DetailCategoryEventDTO dto = modelMapper.map(entity, DetailCategoryEventDTO.class);
        return dto;
    }

    @Override
    public DetailCategoryEvent convertToEntity(Object dto) {
        DetailCategoryEvent entity = modelMapper.map(dto, DetailCategoryEvent.class);
        return entity;
    }
}
