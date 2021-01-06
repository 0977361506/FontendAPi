package com.vnpost.elearning.api.v2.course;

import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.ChapterProcessor;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.processor.CourseProcessor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 04,2020
 */
@RequestMapping("/api/v2/course")
@AllArgsConstructor
@RestController(value = "courseApiV2")
public class CourseApi {
  private CourseProcessor courseProcessor;
  private CourseJoinProcessor courseJoinProcessor;
  private ChapterProcessor chapterProcessor;


  @GetMapping("/total/{courseId}")
  public ResponseEntity<ServiceResult> totalUserInCourse(@PathVariable Long courseId) {
    ServiceResult serviceResult = new ServiceResult();
    serviceResult.setData(courseJoinProcessor.totalUserInCourse(courseId));
    return ResponseEntity.ok(serviceResult);
  }

  @PostMapping("/my-course")
  public ResponseEntity<ServiceResult> myCourseForUser(
      @RequestParam(name = "page", defaultValue = "1", required = false) Integer currentPage,
      @RequestParam(name = "size", defaultValue = "6", required = false) Integer size,
      @Valid @RequestBody CourseDTO courseDTO) {
    Pageable pageable = PageRequest.of(currentPage > 0 ? currentPage - 1 : currentPage, size);
    Long totalItem = courseProcessor.countForMyCourse(courseDTO);
    Integer totalPage = (int) Math.ceil((double) totalItem / size);
    List<CourseDTO> listData = courseProcessor.findAllCourseForMyCourse(courseDTO, pageable);
    ServiceResult serviceResult = new ServiceResult(listData, totalPage, currentPage);
    return ResponseEntity.ok(serviceResult);
  }


  @PostMapping("/course-category/{categoryId}")
  public ResponseEntity<ServiceResult> getCourseByCategory(
      @PathVariable Long categoryId,
      @RequestParam(name = "page", defaultValue = "1", required = false) Integer currentPage,
      @RequestParam(name = "size", defaultValue = "6", required = false) Integer size,
      @RequestBody CourseDTO courseDTO) {
    Pageable pageable = PageRequest.of(currentPage > 0 ? currentPage - 1 : currentPage, size);
    courseDTO.setCategoryId(categoryId);
    Long totalItem = courseProcessor.countByCategoryAndChild(courseDTO);
    Integer totalPage = (int) Math.ceil((double) totalItem / size);
    List<CourseDTO> listData = courseProcessor.findCourseByCategoryAndChild(courseDTO, pageable);
    ServiceResult serviceResult = new ServiceResult(listData, totalPage, currentPage);
    return ResponseEntity.ok(serviceResult);
  }

  @GetMapping("/final/{courseId}")
  public ResponseEntity<ServiceResult> getFinalCourse(@PathVariable Long courseId) {
    return ResponseEntity.ok(new ServiceResult(chapterProcessor.getFinalCourseDTOS(courseId),"success","200"));
  }




}
