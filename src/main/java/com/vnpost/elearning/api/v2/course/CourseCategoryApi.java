package com.vnpost.elearning.api.v2.course;

import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseCategoryProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 03,2020
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/course/category")
public class CourseCategoryApi {
  private CourseCategoryProcessor courseCategoryProcessor;

  @GetMapping
  public ResponseEntity<ServiceResult> findAll( @RequestParam(required = false,name = "parentId") Long parentId) {
    List<CoursecategoryDTO> listData = courseCategoryProcessor.findAll(parentId);
    ServiceResult serviceResult = new ServiceResult();
    serviceResult.setData(listData);
    return ResponseEntity.ok(serviceResult);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ServiceResult> findById(@PathVariable Long id) {
    CoursecategoryDTO data = courseCategoryProcessor.findById(id);
    ServiceResult serviceResult = new ServiceResult();
    serviceResult.setData(data);
    return ResponseEntity.ok(serviceResult);
  }

  @GetMapping("/parent")
  public ResponseEntity<ServiceResult> getListParent() {
    return ResponseEntity.ok(new ServiceResult(courseCategoryProcessor.findByParentIdIsNullAndIsActive(1),"success","200"));
  }

  @GetMapping("/parent/{id}")
  public ResponseEntity<ServiceResult> findByParentId(@PathVariable Long id) {
    return ResponseEntity.ok(new ServiceResult(courseCategoryProcessor.findByParentIdAndIsActive(id,1),"success","200"));
  }


}
