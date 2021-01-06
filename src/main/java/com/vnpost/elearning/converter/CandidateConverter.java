package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.competition.CandidateDTO;
import eln.common.core.entities.competition.Candidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CandidateConverter implements IDTO<CandidateDTO>, IEntity<Candidate> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CandidateDTO convertToDTO(Object entity) {
        CandidateDTO dto = modelMapper.map(entity, CandidateDTO.class);
        return dto;
    }
    public List<CandidateDTO> convertToList(List<Candidate> list) {
        List<CandidateDTO> listd = new ArrayList<>();
        for(Candidate c: list){
            listd.add(convertToDTO(c));
        }
        return  listd;
    }
    @Override
    public Candidate convertToEntity(Object dto) {
        Candidate entity = modelMapper.map(dto, Candidate.class);
        return entity;
    }
}
