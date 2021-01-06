package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.CourseCategoryConverter;
import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import com.vnpost.elearning.service.CourseCategoryService;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Coursecategory;
import eln.common.core.entities.course.QCoursecategory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 03,2020
 */
@Service
@AllArgsConstructor
public class CourseCategoryProcessor {
  private CourseCategoryService courseCategoryService;
  private final QCoursecategory Q = QCoursecategory.coursecategory;
  private CourseCategoryConverter converter;

  public List<CoursecategoryDTO> findAll(Long parentId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.isActive.eq(SystemConstant.ENABLE));
    if (parentId != null) {
      builder.and(Q.parentId.eq(parentId));
    } else {
      builder.and(Q.parentId.isNull());
    }

    return courseCategoryService.findAll(builder, PageRequest.of(0, 99999999)).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public CoursecategoryDTO findById(Long id) {
    Optional<Coursecategory> coursecategoryOptional = courseCategoryService.findById(id);
    if (!coursecategoryOptional.isPresent()) {
      throw new NullPointerException("Không có dữ liệu");
    }
    return converter.convertToDTO(coursecategoryOptional.get());
  }

  public CoursecategoryDTO getParent(Long id) {
    Optional<Coursecategory> coursecategoryOptional = courseCategoryService.findById(id);
    if (!coursecategoryOptional.isPresent()) {
      throw new NullPointerException("Không có dữ liệu");
    }
    return converter.convertToDTO(coursecategoryOptional.get());
  }

  public List<CoursecategoryDTO> findByParentIdIsNullAndIsActive(Integer isActive) {
    return setParent(courseCategoryService.findByParentIdIsNullAndIsActive(isActive).stream().map(converter::convertToDTO).collect(Collectors.toList()));
  }

  private List<CoursecategoryDTO> setParent(List<CoursecategoryDTO> collect) {
    String nameParent = null;
    if(collect.size()>0){
      if(collect.get(0).getParentId()!=null){
        nameParent = courseCategoryService.findById(collect.get(0).getParentId()).get().getName();
      }
    }
    for (CoursecategoryDTO coursecategoryDTO:collect){
        coursecategoryDTO.setIsActive(0);
     if(courseCategoryService.existsByIdAndParentId(coursecategoryDTO.getId(),1)){
        coursecategoryDTO.setIsActive(1);
      }
      coursecategoryDTO.setNameParent(nameParent);
    }
    return collect;
  }

  public List<CoursecategoryDTO> findByParentIdAndIsActive(Long idParent,Integer isActive) {
    return setParent(courseCategoryService.findByParentIdAndIsActive(idParent,isActive).stream().map(converter::convertToDTO).collect(Collectors.toList()));
  }




}
