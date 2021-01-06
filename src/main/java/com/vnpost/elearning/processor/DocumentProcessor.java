package com.vnpost.elearning.processor;

import com.vnpost.elearning.converter.DocumentConverter;
import com.vnpost.elearning.dto.DocumentDTO;
import eln.common.core.exception.DocumentException;
import com.vnpost.elearning.service.CourseJoinService;
import com.vnpost.elearning.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 19,2020
 */
@Service
public class DocumentProcessor {
  @Autowired private DocumentService documentService;
  @Autowired private DocumentConverter converter;
  @Autowired private CourseJoinService courseJoinService;

  public List<DocumentDTO> findByCourseId(Long courseId) {
    return documentService.findByCourseId(courseId).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }
}
