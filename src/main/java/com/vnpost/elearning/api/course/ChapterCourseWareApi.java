package com.vnpost.elearning.api.course;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.ChapterCourseWareProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 21,2020
 */
@RestController
@RequestMapping("/api/chapter-course-ware")
@AllArgsConstructor
public class ChapterCourseWareApi {

  private ChapterCourseWareProcessor chapterCourseWareProcessor;

  @GetMapping("/course/{courseId}")
  public ResponseEntity<ServiceResult> getCourseWareIds(@PathVariable Long courseId) {
    ServiceResult serviceResult = new ServiceResult();
    serviceResult.setData(chapterCourseWareProcessor.getIdsCourseWareByCourseId(courseId));
    return ResponseEntity.ok(serviceResult);
  }
}
