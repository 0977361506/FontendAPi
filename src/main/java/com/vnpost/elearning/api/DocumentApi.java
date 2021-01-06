package com.vnpost.elearning.api;

import com.vnpost.elearning.converter.DocumentCateoryConverter;
import com.vnpost.elearning.converter.DocumentConverter;
import com.vnpost.elearning.dto.DocumentCategoryDTO;
import com.vnpost.elearning.dto.DocumentDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.DocumentProcessor;
import com.vnpost.elearning.service.DocumentCategoryService;
import com.vnpost.elearning.service.DocumentService;
import eln.common.core.exception.DocumentException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DocumentApi {

  @Autowired DocumentCateoryConverter documentCateoryConverter;
  @Autowired DocumentConverter documentConverter;
  @Autowired ServletContext text;
  @Autowired DocumentService documentService;
  @Autowired DocumentCategoryService categoryService;
  @Autowired private DocumentProcessor documentProcessor;


  @PostMapping(value = {"/document/list"})
  public ResponseEntity<ServiceResult> homeDocument(@RequestBody DocumentDTO documentDTO) {
    return new ResponseEntity<>(documentService.findByPropertyDocument(documentDTO),HttpStatus.OK);
  }

  @PostMapping("/document/list-category")
  public ResponseEntity<ServiceResult> categoryDocument(@RequestBody DocumentDTO documentDTO){
    List<DocumentCategoryDTO> dtoList = new ArrayList<>();
    if (documentDTO.getIdCategory() == null){
      dtoList = categoryService.findByParentIsNull();
    }else {
      dtoList = categoryService.findByParent(documentDTO.getIdCategory());
    }
    return ResponseEntity.ok(new ServiceResult(dtoList,"success","200"));
  }

  @GetMapping("/document/category/{id}")
  public ResponseEntity<?> cateDocument(@PathVariable("id")Long id){
    List<DocumentCategoryDTO> dtoList = categoryService.findByParent(id);
    Object[] objects= {dtoList,categoryService.findOne(id)};
    return new ResponseEntity<>(objects,HttpStatus.OK);
  }
  @GetMapping("/document/course/{courseId}")
  public ResponseEntity<ServiceResult> findAllByCourseId(@PathVariable("courseId")Long courseId){
    List<DocumentDTO> result = documentService.findByCourseId(courseId).stream().map(documentConverter::convertToDTO).collect(Collectors.toList());
    return ResponseEntity.ok(new ServiceResult(result,"success","200"));
  }
}
