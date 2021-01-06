package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.DocumentConverter;
import com.vnpost.elearning.dto.AbstractsDTO;
import com.vnpost.elearning.dto.DocumentDTO;
import com.vnpost.elearning.dto.MapDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.repository.AbstractDao;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.document.Document;
import eln.common.core.entities.events.DocumentCategory;
import eln.common.core.repository.document.DocumentCategoryRepository;
import eln.common.core.repository.document.DocumentRepository;
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService extends AbstractDao<Long, Document> {

  @Autowired private DocumentRepository repository;
  @Autowired private DocumentConverter converter;
  @Autowired private DocumentCategoryRepository categoryRepository;

  public ServiceResult findByPropertyDocument(DocumentDTO model) {
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    try {

      if (model.getPage() != null && model.getPage() > 1) {
        model.setFirstItem((model.getPage() - 1) * model.getMaxPageItems());
      }
      AbstractsDTO abstractsDTO =
          new AbstractsDTO(model.getMaxPageItems(), model.getFirstItem(), setValueMapSort(model, myUsers));
      List<DocumentDTO> objects =
          findByProperty(abstractsDTO).stream()
              .map(item -> converter.convertToDTO(item))
              .collect(Collectors.toList());
      setDownload(objects);
      return new ServiceResult(objects, countByProperty(abstractsDTO), "Thanh cong", "200");

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private void setDownload(List<DocumentDTO> objects) {
    for (DocumentDTO documentDTO : objects) {
      if (documentDTO.getAllowedDownload() == 0) {
        documentDTO.setLinkFile(null);
      }
    }
  }

  private MapDTO setValueMapSort(DocumentDTO model, MyUser myUser) {
    MapDTO mapDTO = new MapDTO();
    if (model.getName() != null) {
       likeByKeyAndValue(mapDTO,"name", model.getName());
    }
    if (model.getIdCategory() != null) {
      inByKeyAndValue(mapDTO,"documentCategory.id", setListCategory(model.getIdCategory()));
    }
    if (!(myUser.getPosCode()).equals("1")) {
      equalOrByKeyAndValue(mapDTO,
          new String[] {"shares", "poscodeVnpost.id"}, new Object[] {1, myUser.getPosCode()});
    }
    equalByKeyAndValue(mapDTO,"limitDocument.id",3L);
    equalByKeyAndValue(mapDTO,"status", 1);
    sortByKeyAndValue(mapDTO,"timeCreate","DESC");
    return  mapDTO;
  }

  public DocumentDTO findById(Long id) {
    DocumentDTO documentDTO = converter.convertToDTO(repository.findById(id));
    return documentDTO;
  }

  public List<Document> findByCourseId(Long courseId) {
    return repository.findByCourseId(courseId);
  }

  private List<Long> setListCategory(Long idCategory) {
    List<DocumentCategory> categoryLists = categoryRepository.findAll();
    HashSet<Long> longHashSet = new HashSet<>();
    List<Long> idCategoryNeed = new ArrayList<>();
    List<Long> idTemporarys = new ArrayList<>();
    idTemporarys.add(idCategory);
    idCategoryNeed.add(idCategory);
    getListIdParent(categoryLists, longHashSet, idCategoryNeed, idTemporarys);
    return idCategoryNeed;
  }

  private Integer getListIdParent(
      List<DocumentCategory> categoryLists,
      HashSet<Long> longHashSet,
      List<Long> idCategoryNeed,
      List<Long> idTemporarys) {
    List<Long> idParents = idTemporarys;
    List<Long> idTemporary = new ArrayList<>();
    if (idParents.size() <= 0) {
      return 0;
    }
    for (Long id : idParents) {
      for (DocumentCategory category : categoryLists) {

        if (category.getParent() != null) {
          if ((category.getParent().toString()).equals(id.toString())) {
            if (longHashSet.add(category.getId())) {
              idCategoryNeed.add(category.getId());
              idTemporary.add(category.getId());
            } else {
              return 0;
            }
          }
        }
      }
    }
    idTemporarys = idTemporary;
    return getListIdParent(categoryLists, longHashSet, idCategoryNeed, idTemporarys);
  }
}
