package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.competition.CompetionCategoryDTO;
import eln.common.core.entities.competition.CompetitionCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CompetitionCategoryConverter implements IDTO<CompetionCategoryDTO>, IEntity<CompetitionCategory> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CompetionCategoryDTO convertToDTO(Object entity) {
        CompetionCategoryDTO dto = modelMapper.map(entity, CompetionCategoryDTO.class);
        return dto;
    }

    public List<CompetionCategoryDTO>  convertToList(List<CompetitionCategory> competitionCategories){
        List<CompetionCategoryDTO> competionCategoryDTOS = new ArrayList<>();
        for(CompetitionCategory c:competitionCategories){
            competionCategoryDTOS.add(convertToDTO(c));
        }
        return  competionCategoryDTOS;
    }
    @Override
    public CompetitionCategory convertToEntity(Object dto) {
        CompetitionCategory entity = modelMapper.map(dto, CompetitionCategory.class);
        return entity;
    }
}
