package com.vnpost.elearning.api;

import com.vnpost.elearning.dto.CommentDTO;
import com.vnpost.elearning.dto.ServiceResult;
import eln.common.core.exception.CommentException;
import com.vnpost.elearning.processor.CommentProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 04,2020
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CommentApi {
  private CommentProcessor commentProcessor;

  @PostMapping("/comment/course")
  public ResponseEntity<ServiceResult> create(@Valid @RequestBody CommentDTO commentDTO) {
    ServiceResult serviceResult = new ServiceResult(commentDTO, "Thêm thành công", "200");
    try {
      commentProcessor.create(commentDTO);
    } catch (CommentException e) {
      serviceResult.setMessage(e.getMessage());
      serviceResult.setCode("500");
      return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(serviceResult);
  }

  @GetMapping("/comment/course/{id}")
  public ResponseEntity<ServiceResult> getAllCommentForCourse(
      @PathVariable(name = "id") Long courseId) {
    ServiceResult serviceResult = new ServiceResult("Danh sách bình luận", "200");
    try {
      List<CommentDTO> listData = commentProcessor.findByCourseId(courseId);
      serviceResult.setData(listData);
    } catch (CommentException e) {
      serviceResult.setMessage(e.getMessage());
      serviceResult.setCode("500");
      return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(serviceResult);
  }
}
