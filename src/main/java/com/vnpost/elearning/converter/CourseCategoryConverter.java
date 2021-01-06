package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import eln.common.core.entities.course.Coursecategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseCategoryConverter implements IDTO<CoursecategoryDTO>, IEntity<Coursecategory> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CoursecategoryDTO convertToDTO(Object entity) {
        Coursecategory coursecategory = (Coursecategory) entity;
        CoursecategoryDTO dto = modelMapper.map(coursecategory, CoursecategoryDTO.class);
        return dto;
    }

    @Override
    public Coursecategory convertToEntity(Object dto) {
        CoursecategoryDTO coursecategoryDTO = (CoursecategoryDTO) dto;
        Coursecategory entity = modelMapper.map(coursecategoryDTO, Coursecategory.class);
        return entity;
    }

    public List<CoursecategoryDTO> convertList(List<Coursecategory> list2) {
        List<CoursecategoryDTO>  coursecategories= new ArrayList<>();
        for(Coursecategory c: list2){
            coursecategories.add(convertToDTO(c));
        }
        return  coursecategories;
    }
}
