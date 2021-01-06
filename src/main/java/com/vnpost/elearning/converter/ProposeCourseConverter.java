package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.ProposeCourseDTO;
import com.vnpost.elearning.dto.course.RegisterCourseDTO;
import eln.common.core.entities.course.ProposeCourse;
import eln.common.core.entities.course.RegisterCourse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProposeCourseConverter implements IDTO<ProposeCourseDTO>, IEntity<ProposeCourse>  {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProposeCourseDTO convertToDTO(Object entity) {
        ProposeCourseDTO dto = modelMapper.map(entity, ProposeCourseDTO.class);
        return dto;
    }

    @Override
    public ProposeCourse convertToEntity(Object dto) {
        ProposeCourse en = modelMapper.map(dto, ProposeCourse.class);
        return en;
    }

}
