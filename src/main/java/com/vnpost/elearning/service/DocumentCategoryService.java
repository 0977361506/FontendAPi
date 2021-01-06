package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.DocumentCateoryConverter;
import com.vnpost.elearning.dto.DocumentCategoryDTO;
import eln.common.core.entities.events.DocumentCategory;
import eln.common.core.repository.document.DocumentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentCategoryService {

  @Autowired private DocumentCategoryRepository documentCategoryRepository;
  @Autowired private DocumentCateoryConverter documentCateoryConverter;

  public List<DocumentCategoryDTO> findALl() {
    return documentCategoryRepository.findAll().stream()
        .map(item -> documentCateoryConverter.convertToDTO(item))
        .collect(Collectors.toList());
  }

  public List<DocumentCategory> findAll() {
    return documentCategoryRepository.findAll();
  }

  public DocumentCategoryDTO findOne(long id) {
    DocumentCategoryDTO categoryDTO = documentCateoryConverter.convertToDTO(documentCategoryRepository.findById(id).get());
    return categoryDTO;
  }

  public DocumentCategory findById(Long id) {
    return documentCategoryRepository.findById(id).get();
  }

  public List<DocumentCategoryDTO> findByParentIsNull() {
    return documentCategoryRepository.findByParentIsNull().stream()
        .map(item -> documentCateoryConverter.convertToDTO(item))
        .collect(Collectors.toList());
  }

  public List<DocumentCategoryDTO> findByParent(Long id) {
    return documentCategoryRepository.findByParent(id).stream()
        .map(item -> documentCateoryConverter.convertToDTO(item))
        .collect(Collectors.toList());
  }
}
