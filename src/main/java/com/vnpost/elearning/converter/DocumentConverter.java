package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.DocumentDTO;
import eln.common.core.entities.document.Document;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentConverter implements IEntity<Document>, IDTO<DocumentDTO> {
  @Autowired private ModelMapper modelMapper;

  @Override
  public DocumentDTO convertToDTO(Object entity) {
    Document document = (Document) entity;
    DocumentDTO documentDTO = modelMapper.map(document, DocumentDTO.class);
    documentDTO.setTimeCreate(document.getTimeCreate());
    if (document.getPoscodeVnpost() != null) {
      documentDTO.setId_unit(document.getPoscodeVnpost().getId());
    }
    if (document.getPoscodeVnpost() != null) {
      documentDTO.setPoscodeName(document.getPoscodeVnpost().getName());
    }
    return documentDTO;
  }

  public List<DocumentDTO> convertToList(List<Document> docuent) {
    List<DocumentDTO> documentDTOS = new ArrayList<>();
    for (Document c : docuent) {
      documentDTOS.add(convertToDTO(c));
    }
    return documentDTOS;
  }

  @Override
  public Document convertToEntity(Object dto) {
    //        Document document = modelMapper.map(dto,Document.class);
    ////        return document;
    Document courseDTO = (Document) dto;
    Document entity = modelMapper.map(courseDTO, Document.class);
    // entity.setCourseConfig(null);
    // entity.setOutline(null);

    return entity;
  }
}
