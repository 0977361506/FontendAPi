package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.CourseConfigDTO;
import eln.common.core.entities.course.CourseConfig;
import eln.common.core.uitils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseConfigConverter implements IDTO<CourseConfigDTO>, IEntity<CourseConfig> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseConfigDTO convertToDTO(Object entity) {
        CourseConfigDTO dto = modelMapper.map(entity, CourseConfigDTO.class);
        return dto;
    }

    @Override
    public CourseConfig convertToEntity(Object dto) {
        CourseConfigDTO courseConfigDTO = (CourseConfigDTO) dto;
        CourseConfig enity = modelMapper.map(courseConfigDTO, CourseConfig.class);
        if (StringUtils.isNotBlank(courseConfigDTO.getEnd()) && StringUtils.isNotBlank(courseConfigDTO.getStart())) {
            enity.setStarts(DateUtils.getDate(courseConfigDTO.getStart()));
            enity.setEnds(DateUtils.getDate(courseConfigDTO.getEnd()));
        }

        return enity;
    }
}
