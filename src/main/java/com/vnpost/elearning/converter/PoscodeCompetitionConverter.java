package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.PoscodeCompetitionDTO;

import eln.common.core.entities.competition.PoscodeCompetition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PoscodeCompetitionConverter implements IDTO<PoscodeCompetitionDTO>, IEntity<PoscodeCompetition>  {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PoscodeCompetitionDTO convertToDTO(Object entity) {
        PoscodeCompetitionDTO dto = modelMapper.map(entity, PoscodeCompetitionDTO.class);
        return dto;
    }

    @Override
    public PoscodeCompetition convertToEntity(Object dto) {
        PoscodeCompetition entity = modelMapper.map(dto, PoscodeCompetition.class);
        return entity;
    }

}
