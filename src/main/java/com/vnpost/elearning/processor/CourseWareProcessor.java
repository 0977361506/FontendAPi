package com.vnpost.elearning.processor;

import com.vnpost.elearning.converter.CourseWareConverter;
import com.vnpost.elearning.dto.course.CourseWareDTO;
import eln.common.core.exception.CourseWareException;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CourseJoinService;
import com.vnpost.elearning.service.CourseWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 10,2020
 */
@Service
public class CourseWareProcessor {

  @Autowired private CourseWareService courseWareService;
  @Autowired private CourseWareConverter converter;
  @Autowired private CourseJoinService courseJoinService;

  public List<CourseWareDTO> findByChapterId(Long chapterId, Long courseId)
      throws CourseWareException {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!courseJoinService.existUserInCourse(myUser.getId(), courseId)) {
      throw new CourseWareException("Cần tham gia khóa học");
    }
    return courseWareService.findByChapterId(chapterId).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }
}
