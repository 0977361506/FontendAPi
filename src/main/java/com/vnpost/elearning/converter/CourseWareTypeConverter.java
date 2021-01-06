package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.CourseWareTypeDTO;
import eln.common.core.entities.courseware.CourseWareType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseWareTypeConverter implements IDTO<CourseWareTypeDTO>, IEntity<CourseWareType> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseWareTypeDTO convertToDTO(Object entity) {
        CourseWareType courseWareType = (CourseWareType) entity;
        CourseWareTypeDTO courseWareTypeDTO = modelMapper.map(courseWareType, CourseWareTypeDTO.class);
        return courseWareTypeDTO;
    }

    @Override
    public CourseWareType convertToEntity(Object dto) {
        CourseWareTypeDTO courseWareTypeDTO = (CourseWareTypeDTO) dto;
        CourseWareType courseWareType = modelMapper.map(courseWareTypeDTO, CourseWareType.class);
        return courseWareType;
    }
}
