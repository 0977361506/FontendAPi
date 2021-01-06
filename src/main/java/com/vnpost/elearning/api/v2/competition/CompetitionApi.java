package com.vnpost.elearning.api.v2.competition;

import com.vnpost.elearning.dto.competition.CompetitionDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CompetitionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "competitionApiV2")
@RequestMapping("/api/v2/competition")
public class CompetitionApi {
  @Autowired private CompetitionProcessor competitionProcessor;

  @PostMapping("/list/all")
  public ResponseEntity<ServiceResult> getAllCompetition(
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "1") Integer page,
      @RequestBody CompetitionDTO competitionDTO) {
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
    Long totalItem = null;
    try {
      totalItem = competitionProcessor.countAll(competitionDTO);
      Integer totalPage = (int) Math.ceil((double) totalItem / size);
      List<CompetitionDTO> listCourse = competitionProcessor.findAll(competitionDTO, pageable);
      ServiceResult serviceResult = new ServiceResult(listCourse, totalPage, page);
      return new ResponseEntity<>(serviceResult, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ServiceResult(e.getMessage(),"400"), HttpStatus.BAD_REQUEST);
    }


  }

  @GetMapping("/categories/all")
  public ResponseEntity<ServiceResult> getAllCompetitionCategory(
      @RequestParam(required = false,name = "parentId") Long parentId) {
    return new ResponseEntity<>(
        new ServiceResult(competitionProcessor.findAllCategory(parentId), "success", "200"),
        HttpStatus.OK);
  }
}
