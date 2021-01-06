package com.vnpost.elearning.processor;

import com.vnpost.elearning.service.RateService;
import eln.common.core.entities.course.Rate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 07,2020
 */
@Service
@AllArgsConstructor
public class RateProcessor {
  private RateService rateService;

  public Float getAverageRateByCourseId(Long courseId) {
    List<Rate> rateList = rateService.findByCourseId(courseId);
    if (rateList.size() == 0) {
      return 0f;
    }
    Integer totalRate = 0;
    for (Rate rate : rateList) {
      totalRate += rate.getValuess();
    }
    return totalRate / (float) rateList.size();
  }
}
