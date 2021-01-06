package com.vnpost.elearning.api.v2.course;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.RateProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 07,2020
 */
@RestController(value = "ratingApiV2")
@RequestMapping("/api/v2/course/rating")
@AllArgsConstructor
public class RatingApi {
  private RateProcessor rateProcessor;
  private RateService rateService;

  @GetMapping("/{courseId}")
  public ResponseEntity<ServiceResult> getRating(@PathVariable Long courseId) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ServiceResult serviceResult = new ServiceResult();
    Map<String,Object> result = new HashMap<>();
    result.put("avarageRate",rateProcessor.getAverageRateByCourseId(courseId));
    result.put("isUserRated",rateService.isUserRated(myUser.getId(),courseId));
    serviceResult.setData(result);
    return ResponseEntity.ok(serviceResult);
  }
}
