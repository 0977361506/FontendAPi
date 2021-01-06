package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.CourseWareProcessDTO;
import eln.common.core.entities.courseware.CourseWareProcess;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseWareProcessConverter implements IEntity<CourseWareProcess>, IDTO<CourseWareProcessDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseWareProcessDTO convertToDTO(Object entity) {
        CourseWareProcess courseWareProcess = (CourseWareProcess) entity;
        CourseWareProcessDTO courseWareProcessDTO = modelMapper.map(courseWareProcess, CourseWareProcessDTO.class);
        return courseWareProcessDTO;
    }

    @Override
    public CourseWareProcess convertToEntity(Object dto) {
        CourseWareProcessDTO courseWareProcessDTO = (CourseWareProcessDTO) dto;
        CourseWareProcess courseWareProcess = modelMapper.map(courseWareProcessDTO, CourseWareProcess.class);
        return courseWareProcess;
    }
}
