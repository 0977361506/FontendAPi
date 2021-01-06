package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.CourseWareTypeConverter;
import com.vnpost.elearning.dto.course.CourseWareTypeDTO;
import eln.common.core.entities.courseware.CourseWareType;
import eln.common.core.repository.CourseWareTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseWareTypeService {
    @Autowired
    private CourseWareTypeRepository courseWareTypeRepository;
    @Autowired
    private CourseWareTypeConverter converter;


    public List<CourseWareTypeDTO> findAll() {

        return courseWareTypeRepository.findAll()
                .stream().map(item -> converter.convertToDTO(item)).collect(Collectors.toList());
    }


    public CourseWareType findById(Long id) {
        return courseWareTypeRepository.findById(id).get();
    }


    public CourseWareType findByCode(String code) {
        return courseWareTypeRepository.findByCode(code);
    }
}
