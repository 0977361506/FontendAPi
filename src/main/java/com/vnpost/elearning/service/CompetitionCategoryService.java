package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.CompetitionCategoryConverter;
import com.vnpost.elearning.dto.competition.CompetionCategoryDTO;
import com.vnpost.elearning.dto.competition.CompetitionDTO;
import eln.common.core.entities.competition.CompetitionCategory;
import eln.common.core.repository.competition.CompetitionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionCategoryService extends CommonRepository<CompetitionCategory,CompetitionCategoryRepository> {
    @Autowired
    EntityManager entityManager;
    @Autowired
    private CompetitionCategoryConverter categoryConverter;
    @Autowired
    private CompetitionCategoryRepository cate;

    public CompetitionCategoryService(CompetitionCategoryRepository repo) {
        super(repo);
    }

    public List<CompetionCategoryDTO> findParentOn(CompetitionDTO competitionDTO) {
        if(competitionDTO.getCategory_value()==null){
            return cate.findByParentIsNullAndStatus(0)
                    .stream().map(item -> categoryConverter.convertToDTO(item)).collect(Collectors.toList()) ;
        }else
        {
            return cate.findByParentAndStatus(Long.parseLong(competitionDTO.getCategory_value()),0)
                    .stream().map(item -> categoryConverter.convertToDTO(item)).collect(Collectors.toList()) ;
        }

    }


    public CompetionCategoryDTO findByIds(long id) {
        CompetionCategoryDTO categoryDTO = categoryConverter.convertToDTO(cate.findById(id).get());
        return categoryDTO;
    }


    public String save(CompetionCategoryDTO categoryDTO) {
        Integer count = cate.countByName(categoryDTO.getNameCompetition());
        if (count == 0) {
            categoryDTO.setStatus(0);
            categoryDTO.setTimeCreate(Calendar.getInstance().getTime());
            categoryDTO.setLastUpdate(Calendar.getInstance().getTime());

            CompetitionCategory category = categoryConverter.convertToEntity(categoryDTO);
            cate.save(category);

            return "0";
        }
        return "1";
    }



}
