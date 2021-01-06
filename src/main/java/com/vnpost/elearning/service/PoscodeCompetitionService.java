package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.PoscodeCompetitionConverter;
import com.vnpost.elearning.dto.competition.PoscodeCompetitionDTO;

import eln.common.core.entities.competition.PoscodeCompetition;
import eln.common.core.repository.PoscodeCompetitionRepositiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PoscodeCompetitionService {
    @Autowired
    private PoscodeCompetitionRepositiry poscodeCompetitionRepositiry;
    @Autowired
    private PoscodeCompetitionConverter poscodeCompetitionConverter;

    public List<PoscodeCompetitionDTO>  findByIdCompetition(Long id) {
        List<PoscodeCompetition> poscodeCompetitionList = poscodeCompetitionRepositiry.findByIdCompetition(id);
        List<PoscodeCompetitionDTO> listDTOs = new ArrayList<>();
        for (PoscodeCompetition entity :poscodeCompetitionList){
            listDTOs.add(poscodeCompetitionConverter.convertToDTO(entity));
        }
        return listDTOs;
    }
    public List<String>  findNameByIdCompetition(Long id) {
        try {
            return poscodeCompetitionRepositiry.findNameByIdCompetition(id);
        }catch (Exception e){
            return null;
        }
    }


}
