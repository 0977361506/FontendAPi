package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.TypeQuestionConverter;
import com.vnpost.elearning.dto.competition.TypeQuestionDTO;
import eln.common.core.entities.question.TypeQuestion;
import eln.common.core.repository.TypeQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeQuestionService {

    @Autowired
    private TypeQuestionRepository typeQuestionRepository;
    @Autowired
    private TypeQuestionConverter typeQuestionConverter;


    public List<TypeQuestionDTO> getListAll() {
        List<TypeQuestion> list = typeQuestionRepository.getListAll();
        List<TypeQuestionDTO> typeQuestionDTOS = new ArrayList<>();
        for (TypeQuestion typeQuestion : list) {
            TypeQuestionDTO toDTO = typeQuestionConverter.convertToDTO(typeQuestion);
            typeQuestionDTOS.add(toDTO);
        }
        return typeQuestionDTOS;
    }


    public TypeQuestion findById(Long id) {
        return typeQuestionRepository.findById(id).get();
    }
}
