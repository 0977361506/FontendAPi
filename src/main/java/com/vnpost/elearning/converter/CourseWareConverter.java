package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.CourseWareDTO;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareType;
import eln.common.core.repository.CourseWareTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CourseWareConverter implements IDTO<CourseWareDTO>, IEntity<CourseWare> {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CourseWareTypeRepository courseWareTypeRepository;


    public CourseWareDTO convertToDTO(CourseWare entity) {
        CourseWareDTO courseWareDTO = modelMapper.map(entity, CourseWareDTO.class);
        return courseWareDTO;
        //     LoggerAdapter
    }

    public List<CourseWareDTO> convertToList(List<CourseWare> courseWares){
        List<CourseWareDTO> courseWareDTOS = new ArrayList<>();
        for(CourseWare c: courseWares){
            courseWareDTOS.add(convertToDTO(c));
        }
        return  courseWareDTOS;
    }
    @Override
    public CourseWare convertToEntity(Object dto) {
        CourseWareDTO courseWareDTO = (CourseWareDTO) dto;
        CourseWare courseWare = modelMapper.map(courseWareDTO, CourseWare.class);
        CourseWareType courseWareType = courseWareTypeRepository.findByCode(courseWareDTO.getTypeCourseWareCode());
        courseWare.setCourseWareType(courseWareType);
        courseWare.setCreatedDate(new Date());
        //  courseWare.setId_poscode(courseWareDTO.getId_poscode());
        return courseWare;
    }
}

