package com.vnpost.elearning.converter;

import com.vnpost.elearning.Beans.CourseBeanDTO;
import com.vnpost.elearning.dto.course.CourseDTO;
import eln.common.core.entities.course.Course;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseConverter implements IDTO<CourseDTO>, IEntity<Course> {
  @Autowired private ModelMapper modelMapper;

  public CourseBeanDTO convertToDTOBean(CourseDTO courseDTO) {
    CourseBeanDTO dto = modelMapper.map(courseDTO, CourseBeanDTO.class);

    return dto;
  }

  public List<CourseBeanDTO> convertToListBeanDto(List<CourseDTO> courseDTOS) {
    List<CourseBeanDTO> courseBeanDTOS = new ArrayList<>();
    for (CourseDTO courseDTO : courseDTOS) {
      courseBeanDTOS.add(convertToDTOBean(courseDTO));
    }
    return courseBeanDTOS;
  }

  @Override
  public CourseDTO convertToDTO(Object entity) {
    Course course = (Course) entity;
    CourseDTO dto = modelMapper.map(course, CourseDTO.class);
    if (course.getCoursecategory() != null) {
      dto.setCategoryId(course.getCoursecategory().getId());
      dto.setCategoryName(course.getCoursecategory().getName());
    }
    if (course.getPoscodeVnpost() != null) {
      dto.setPcode(course.getPoscodeVnpost().getId());
      dto.setPoscodeName(course.getPoscodeVnpost().getName());
    }
    return dto;
  }

  @Override
  public Course convertToEntity(Object dto) {
    CourseDTO courseDTO = (CourseDTO) dto;
    Course entity = modelMapper.map(courseDTO, Course.class);
    entity.setCourseConfig(null);
    entity.setOutline(null);

    return entity;
  }

  public List<CourseDTO> convertList(List<Course> courses) {
    List<CourseDTO> courseDTOS = new ArrayList<>();
    for (Course c : courses) {
      courseDTOS.add(convertToDTO(c));
    }
    return courseDTOS;
  }
}
