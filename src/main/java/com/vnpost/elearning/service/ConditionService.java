package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.ConditionConverter;
import com.vnpost.elearning.dto.competition.ConditionDTO;
import eln.common.core.entities.competition.Condition;
import eln.common.core.repository.ConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ConditionService {
    @Autowired
    private ConditionRepository conditionRepository;
    @Autowired
    private ConditionConverter conditionConverter;


    public List<ConditionDTO> findAll() {
        List<Condition> conditions = conditionRepository.findAll();
        List<ConditionDTO> lConditionDTOs = new ArrayList<ConditionDTO>();
        for (Condition condition : conditions) {
            lConditionDTOs.add(conditionConverter.convertToDTO(condition));
        }
        return lConditionDTOs;
    }

}
