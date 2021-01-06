package com.vnpost.elearning.api.v2.blog;

import com.vnpost.elearning.dto.NewsDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.NewsProcessor;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 10,2020
 */
@AllArgsConstructor
@RestController(value = "newsApiV2")
@RequestMapping("/api/v2/news")
public class NewsApi {
  private NewsProcessor newsProcessor;

  @PostMapping("/all")
  public ResponseEntity<ServiceResult> findAll(
      @RequestParam(name = "page", defaultValue = "1", required = false) Integer currentPage,
      @RequestParam(name = "size", defaultValue = "6", required = false) Integer size,
      @RequestBody NewsDTO newsDTO) {
    Pageable pageable = PageRequest.of(currentPage > 0 ? currentPage - 1 : 0, size);
    List<NewsDTO> data = newsProcessor.findAll(newsDTO, pageable);
    Long totalItem = newsProcessor.count(newsDTO);
    Integer totalPage = (int) Math.ceil((double) totalItem / size);
    ServiceResult serviceResult = new ServiceResult(data, totalPage, currentPage);
    return ResponseEntity.ok(serviceResult);
  }
}
