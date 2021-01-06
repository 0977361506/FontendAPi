package com.vnpost.elearning.api.course;

import com.vnpost.elearning.api.ExceptionHandlerApi;
import com.vnpost.elearning.dto.course.CourseJoinDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.processor.CourseJoinRequestProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CourseJoinRequestService;
import eln.common.core.exception.CourseJoinException;
import eln.common.core.exception.CourseRequestException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author:Nguyen Anh Tuan
 *     <p>May 30,2020
 */
@RestController
@AllArgsConstructor
public class CourseRequestApi extends ExceptionHandlerApi {

  private CourseJoinRequestService courseJoinRequestService;

  private CourseJoinRequestProcessor courseJoinRequestProcessor;
  private CourseJoinProcessor courseJoinProcessor;
  private final Logger logger = LogManager.getLogger(CourseRequestApi.class);

  @DeleteMapping("/api/delete/course/request/{idcourse}")
  public ResponseEntity<ServiceResult> deleteRequest(@PathVariable("idcourse") Long idcourse) {
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    int slrowDelete =
        courseJoinRequestService.deleteRequestByUserAndIdCourse(myUsers.getId(), idcourse);
    if (slrowDelete > 0) {
      ServiceResult serviceResult = new ServiceResult("Hủy đăng kí thành công !", "200");
      return ResponseEntity.ok(serviceResult);
    } else {
      ServiceResult serviceResult = new ServiceResult("Hủy đăng kí thất bại !", "500");
      return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/api/course/request/{id}")
  public ResponseEntity<ServiceResult> requestCourse(@PathVariable(name = "id") Long coursId) {
    ServiceResult serviceResult =
        new ServiceResult(
            "Đăng ký khóa học thành công, vui lòng chờ quản trị phê duyệt yêu cầu!", "200");
    try {
      courseJoinRequestProcessor.createRequest(coursId);
    } catch (CourseRequestException e) {
      serviceResult.setMessage(e.getMessage());
      serviceResult.setCode("500");
      logger.warn(e.getMessage());
      return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
    } catch (NullPointerException nullpoint) {
      serviceResult.setCode("500");
      logger.warn(nullpoint.getMessage());
      return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(serviceResult);
  }

  @PostMapping("/api/course/request-code")
  public ResponseEntity<ServiceResult> requestWithCode(
      @Valid @RequestBody CourseJoinDTO courseJoinDTO) {
    ServiceResult serviceResult = new ServiceResult("true", "200");
    try {
      courseJoinProcessor.joinWithCode(courseJoinDTO);
    } catch (CourseJoinException e) {
      logger.info(e.getMessage());
      serviceResult.setMessage(e.getMessage());
      serviceResult.setCode("500");
      return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(serviceResult);
  }
}
