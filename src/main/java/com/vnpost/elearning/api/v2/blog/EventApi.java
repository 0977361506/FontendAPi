package com.vnpost.elearning.api.v2.blog;

import com.vnpost.elearning.dto.EventDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.EventProcessor;
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
@RestController(value = "eventApiv2")
@RequestMapping("/api/v2/event")
public class EventApi {
  private EventProcessor eventProcessor;

  @PostMapping("/all")
  public ResponseEntity<ServiceResult> findAll(
      @RequestParam(name = "page", defaultValue = "1", required = false) Integer currentPage,
      @RequestParam(name = "size", defaultValue = "6", required = false) Integer size,
      @RequestBody EventDTO eventDTO) {
    Pageable pageable = PageRequest.of(currentPage > 0 ? currentPage - 1 : 0, size);
    List<EventDTO> listData = eventProcessor.findALl(eventDTO, pageable);
    Long totalItem = eventProcessor.count(eventDTO);
    Integer totalPage = (int) Math.ceil((double) totalItem / size);
    ServiceResult serviceResult = new ServiceResult(listData, totalPage, currentPage);
    serviceResult.setMessage("Danh sách sự kiện");
    serviceResult.setCode("200");
    return ResponseEntity.ok(serviceResult);
  }
}
