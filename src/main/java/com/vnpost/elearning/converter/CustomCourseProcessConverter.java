package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.customDTO.CustomCourseProcessDTO;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import org.springframework.stereotype.Component;

@Component
public class CustomCourseProcessConverter {

    public CustomCourseProcessDTO converter (CourseWareProcess courseWareProcess) {

        CustomCourseProcessDTO courseProcessDTO = new CustomCourseProcessDTO();

        courseProcessDTO.setId(courseWareProcess.getCourseWare().getId());
        courseProcessDTO.setNamecourseWare(courseWareProcess.getCourseWare().getName());
        courseProcessDTO.setNamecourseWareType(courseWareProcess.getCourseWare().getCourseWareType().getName());
        courseProcessDTO.setStatus(courseWareProcess.getStatus());
        return courseProcessDTO;
    }

    public CustomCourseProcessDTO converter (CourseWare courseWare) {

        CustomCourseProcessDTO courseProcessDTO = new CustomCourseProcessDTO();
        courseProcessDTO.setId(courseWare.getId());
        courseProcessDTO.setNamecourseWare(courseWare.getName());
        courseProcessDTO.setNamecourseWareType(courseWare.getCourseWareType().getName());
        courseProcessDTO.setStatus(0);
        return courseProcessDTO;
    }


}
