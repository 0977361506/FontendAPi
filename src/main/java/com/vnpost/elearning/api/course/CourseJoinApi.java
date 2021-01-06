package com.vnpost.elearning.api.course;

import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.security.MyUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 04,2020
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CourseJoinApi {
  private CourseJoinProcessor courseJoinProcessor;

  @PostMapping("/course/join")
  public ResponseEntity<ServiceResult> joinFreeCourse(@RequestBody CourseDTO courseDTO) {
    ServiceResult result = new ServiceResult("success", "200");
    try {
      courseJoinProcessor.joinFreeCourse(courseDTO);
    } catch (Exception e) {
      result.setCode("500");
      result.setMessage(e.getMessage());
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("/course/courseJoin/{id}/currentUser")
  public ResponseEntity<ServiceResult> checkCurrentUserInCourse(@PathVariable Long id) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ServiceResult result = new ServiceResult();
    if (courseJoinProcessor.exitsUserAndCourse(myUser.getId(), id)) {
      result.setData(true);
      result.setMessage("true");
    } else {
      result.setData(false);
      result.setMessage("false");
    }
    result.setCode("200");
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}
