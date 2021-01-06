package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.course.RegisterCourseDTO;
import eln.common.core.entities.course.RegisterCourse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RegisterCourseConverter implements IDTO<RegisterCourseDTO>, IEntity<RegisterCourse> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RegisterCourseDTO convertToDTO(Object entity) {
        RegisterCourseDTO dto = modelMapper.map(entity, RegisterCourseDTO.class);
        return dto;
    }

    @Override
    public RegisterCourse convertToEntity(Object dto) {
        RegisterCourse en = modelMapper.map(dto, RegisterCourse.class);
        return en;
    }

}
