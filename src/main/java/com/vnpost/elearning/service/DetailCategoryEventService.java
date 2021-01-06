package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.DetailCategoryEventConverter;
import com.vnpost.elearning.dto.DetailCategoryEventDTO;
import eln.common.core.entities.document.DetailCategoryEvent;
import eln.common.core.repository.blog.DetailCategoryEventRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetailCategoryEventService {
    @Autowired
    DetailCategoryEventRepository d;
    @Autowired
    private DetailCategoryEventConverter detailCategoryEventConverter;
    public List<DetailCategoryEvent> findAll() {
        return d.findAll();
    }

    public  List<DetailCategoryEventDTO> findByStatus(){
        return d.findByStatus().stream().map(item -> detailCategoryEventConverter.convertToDTO(item)).collect(Collectors.toList());
    }

    public  List<DetailCategoryEventDTO> findByParent(Long idParent){
        return d.findByParentIdAndIsActive(idParent,1).stream().map(item -> detailCategoryEventConverter.convertToDTO(item)).collect(Collectors.toList());
    }

    public DetailCategoryEvent findById(Long id) {
        return d.findById(id).get();
    }


    public List<DetailCategoryEvent> findAll(DetailCategoryEvent detailCategoryEvent) {
        if (StringUtils.isNotEmpty(detailCategoryEvent.getNameDetail())) {
            return d.findByNameDetailContaining(detailCategoryEvent.getNameDetail());
        } else {
            return d.findAll();
        }

    }


    public DetailCategoryEvent save(DetailCategoryEvent detailCategoryEvent) {
        return d.save(detailCategoryEvent);
    }


    public void deleteById(Long id) {
        d.deleteById(id);
    }

    public List<DetailCategoryEvent> getListCategoryEventParent() {
        return d.findByParentIdIsNullAndIsActive(1);
    }

    public List<DetailCategoryEventDTO> findParent() {
        return d.findByParentIdIsNullAndIsActive(1).stream()
                .map(detailCategoryEventConverter::convertToDTO)
                .collect(Collectors.toList());
    }
}
