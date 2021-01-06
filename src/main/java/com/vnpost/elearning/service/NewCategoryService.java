package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.NewCategoryConverter;
import com.vnpost.elearning.dto.NewCategoryDTO;
import eln.common.core.entities.news.NewCategory;
import eln.common.core.repository.blog.NewCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewCategoryService {
    @Autowired
    private  NewCategoryRepository newCategoryRepository;
    @Autowired
    private NewCategoryConverter newCategoryConverter;
    @Autowired
    EntityManagerFactory entityManagerFactory;

    public NewCategory save(NewCategory entity) {
        return newCategoryRepository.save(entity);
    }

    public <S extends NewCategory> Optional<S> findOne(Example<S> example) {
        return newCategoryRepository.findOne(example);
    }

    public Page<NewCategory> findAll(Pageable pageable) {
        return newCategoryRepository.findAll(pageable);
    }

    public List<NewCategory> findAll() {
        return newCategoryRepository.findAll();
    }


    public List<NewCategory> findAll(NewCategory newCategory) {
        if (StringUtils.isNotBlank(newCategory.getNameDetail())) {
            return newCategoryRepository.findByNameDetailContaining(newCategory.getNameDetail());
        } else {
            return findAll();
        }

    }

    public List<NewCategory> findAll(Sort sort) {
        return newCategoryRepository.findAll(sort);
    }

    public List<NewCategoryDTO> findParent() {
        return newCategoryRepository
                .findByParentIdIsNullAndIsActive(1).stream().map(item->newCategoryConverter.convertToDTO(item)).collect(Collectors.toList());
    }


    public Optional<NewCategory> findById(Long id) {
        return newCategoryRepository.findById(id);
    }

    public List<NewCategoryDTO> findByParent(Long id) {
        return newCategoryRepository
                .findByParentIdAndIsActive(id,1).stream().map(item->newCategoryConverter.convertToDTO(item)).collect(Collectors.toList());
    }

 }
