package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.QuestionCategoryConverter;
import com.vnpost.elearning.dto.competition.QuestionCategoryDTO;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.question.QuestionCategory;
import eln.common.core.repository.question.QuestionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionCategoryService {

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;
    @Autowired
    private QuestionCategoryConverter questionCategoryConverter;

    @Autowired
    private PoscodeVnpostService poscodeVnpostService;


    public List<QuestionCategoryDTO> getListByIdUnit(String id) {

        List<QuestionCategoryDTO> list = new ArrayList<>();
        try {


            List<QuestionCategory> questionCategories = questionCategoryRepository.getListByIdUnit(id);

            List<QuestionCategory> listSearch = questionCategoryRepository.getListBySearch(SystemConstant.SEARCH_YES, id);
            if (questionCategories.size() > 0) {
                for (QuestionCategory category : questionCategories) {
                    QuestionCategoryDTO questionCategoryDTO = questionCategoryConverter.convertToDTO(category);
                    list.add(questionCategoryDTO);
                }
            }
            if (listSearch.size() > 0) {
                for (QuestionCategory questionCategory : listSearch) {
                    list.add(questionCategoryConverter.convertToDTO(questionCategory));
                }
            }

        } catch (Exception e) {
            e.getMessage();
            e.fillInStackTrace();
            return null;
        }
        return list;
    }


    public List<QuestionCategory> getListBySearch(String shares) {

        return null;


    }


    public List<QuestionCategory> findAll() {
        return questionCategoryRepository.findAll();
    }


    public QuestionCategory findById(Long id) {
        return questionCategoryRepository.findById(id).get();
    }


    public QuestionCategoryDTO getOne(Long id) {
        return questionCategoryConverter.convertToDTO(findById(id));
    }


    public List<QuestionCategory> findByNameContaining(String name) {
        if (name == null) {
            return questionCategoryRepository.findAll();
        }
        return questionCategoryRepository.findByNameCategoryContaining(name);
    }

}
