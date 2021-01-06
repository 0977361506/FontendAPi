package com.vnpost.elearning.api;

import com.vnpost.elearning.Beans.Dates;
import com.vnpost.elearning.converter.DetailCategoryEventConverter;
import com.vnpost.elearning.dto.DetailCategoryEventDTO;
import com.vnpost.elearning.dto.EventDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.service.DetailCategoryEventService;
import com.vnpost.elearning.service.EventService;
import com.vnpost.elearning.service.ResultService;
import eln.common.core.entities.document.DetailCategoryEvent;
import eln.common.core.entities.events.Eventt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventApi {
  @Autowired private Dates d;
  @Autowired private EventService eventService;
  @Autowired private DetailCategoryEventService eventCategoryService;
  @Autowired private DetailCategoryEventConverter detailCategoryEventConverter;

  @RequestMapping("/event/chitiet/{id}") // chi tiết su kiên
  public ResponseEntity<Object[]> chitiet(@PathVariable("id") Long id) {
    Eventt eventt = eventService.findById(id).get();
    List<EventDTO> eventDTOList = eventService.getEnvetTopBest();
    List<DetailCategoryEventDTO> list2 = eventCategoryService.findParent();
    Object[] objects = {list2, eventDTOList, eventt};
    return new ResponseEntity<>(objects, HttpStatus.OK);
  }


  @PostMapping("/home/events")
  public ResponseEntity<ServiceResult> event(@RequestBody EventDTO eventDTO) {
    Pageable pageable = PageRequest.of(eventDTO.getPage() > 0 ? eventDTO.getPage() - 1 : 0, eventDTO.getMaxPageItems());
    Object[] objects = eventService.getListEvents(eventDTO,pageable);
    return new ResponseEntity<>(new ServiceResult((List<EventDTO>) objects[0], Long.parseLong(objects[1].toString()),"Thanh cong","200") , HttpStatus.OK);
  }

  @GetMapping("/event/category/list")
  public ResponseEntity<ServiceResult> ListCategory() {
    List<DetailCategoryEvent> categoryEvents =
        eventCategoryService
            .getListCategoryEventParent();
    return ResponseEntity.ok(new ServiceResult(categoryEvents, "success", "200"));
  }

  @GetMapping("/event/list")
  public ResponseEntity<ServiceResult> listEvent() {
    return ResponseEntity.ok(new ServiceResult(eventService.findByStatus(1), "success", "200"));
  }

  @GetMapping("/event/detail/{id}")
  public ResponseEntity<ServiceResult> eventDetail(@PathVariable Long id) {
    return ResponseEntity.ok(
        new ServiceResult(eventService.findByIdAndStatus(id, 1), "success", "200"));
  }

  @GetMapping("/event/list/category/{idCategory}")
  public ResponseEntity<ServiceResult> listEventByCategoryId(@PathVariable Long idCategory) {
    return ResponseEntity.ok(
        new ServiceResult(
            eventService.findAllByCategoryIdAndStatus(idCategory, 1), "success", "200"));
  }

  @GetMapping("/event/list/category/parent")
  public ResponseEntity<ServiceResult> findParent() {
    return ResponseEntity.ok(
            new ServiceResult(
                    eventCategoryService.findParent(), "success", "200"));
  }

  @GetMapping("/event/category/parent/{id}")
  public ResponseEntity<ServiceResult> findByParent(@PathVariable("id") Long id) {
    return ResponseEntity.ok(
            new ServiceResult(
                    eventCategoryService.findByParent(id), "success", "200"));
  }

  @GetMapping("/event/category/{id}")
  public ResponseEntity<ServiceResult> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(
          new ServiceResult(
         detailCategoryEventConverter.convertToDTO(eventCategoryService.findById(id)), "success", "200"));
  }
}
