package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.CourseCategoryConverter;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Coursecategory;
import eln.common.core.repository.course.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCategoryService
    extends CommonRepository<Coursecategory, CourseCategoryRepository> {
  @Autowired private CourseCategoryRepository courseCategoryRepository;
  @Autowired private CourseCategoryConverter converter;
  @Autowired EntityManager entityManager;

  public List<Coursecategory> findAll() {
    return courseCategoryRepository.findAll();
  }

  public List<CoursecategoryDTO> findParent(CourseDTO courseDTO) {
    if (courseDTO.getCategoryId() == null) {
      return courseCategoryRepository.findByParentIdIsNullAndIsActive(SystemConstant.ENABLE)
          .stream()
          .map(converter::convertToDTO)
          .collect(Collectors.toList());
    } else {
      return courseCategoryRepository
          .findByParentIdAndIsActive(courseDTO.getCategoryId(), SystemConstant.ENABLE).stream()
          .map(converter::convertToDTO)
          .collect(Collectors.toList());
    }
  }

  public CourseCategoryService(CourseCategoryRepository repo) {
    super(repo);
  }

  public List<Long> getTreeCategoryByParentId(Long parentId) {
    return repo.getTreeByParentId(parentId);
  }
  public List<Coursecategory> findByParentIdIsNullAndIsActive(Integer isActive) {
    return courseCategoryRepository.findByParentIdIsNullAndIsActive(isActive);
  }

  public List<Coursecategory> findByParentIdAndIsActive(Long idParent,Integer isActive) {
    return courseCategoryRepository.findByParentIdAndIsActive(idParent,isActive);
  }

  public Boolean existsByIdAndParentId(Long idParent,Integer isActive) {
    return courseCategoryRepository.countByParentIdAndIsActive(idParent,isActive)>0?true:false;
  }




}
