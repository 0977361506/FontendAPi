package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.CourseJoinDTO;
import eln.common.core.entities.course.CourseJoin;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseJoinConverter implements IEntity<CourseJoin>, IDTO<CourseJoinDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseJoinDTO convertToDTO(Object entity) {
        CourseJoin courseJoin = (CourseJoin) entity;
        CourseJoinDTO courseJoinDTO = modelMapper.map(courseJoin, CourseJoinDTO.class);
        return courseJoinDTO;
    }

    @Override
    public CourseJoin convertToEntity(Object dto) {
        CourseJoinDTO courseJoinDTO = (CourseJoinDTO) dto;
        CourseJoin courseJoin = modelMapper.map(courseJoinDTO, CourseJoin.class);
        if (courseJoinDTO.getStatus() == null) {
            courseJoin.setStatus(1);
        }
        return courseJoin;
    }
}
