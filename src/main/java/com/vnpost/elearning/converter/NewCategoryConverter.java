package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.NewCategoryDTO;
import eln.common.core.entities.news.NewCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewCategoryConverter  implements IDTO<NewCategoryDTO>, IEntity<NewCategory> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NewCategoryDTO convertToDTO(Object entity) {
        NewCategory newCategory = (NewCategory) entity;
        NewCategoryDTO newCategoryDTO = modelMapper.map(newCategory, NewCategoryDTO.class);
        return newCategoryDTO;
    }

    @Override
    public NewCategory convertToEntity(Object dto) {
        NewCategoryDTO newCategoryDTO = (NewCategoryDTO) dto;
        NewCategory newCategory = modelMapper.map(newCategoryDTO, NewCategory.class);
        return newCategory;
    }
}
