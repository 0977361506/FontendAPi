package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.competition.CompetitionDTO;
import eln.common.core.entities.competition.Competition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompetitionConverter implements IDTO<CompetitionDTO>, IEntity<Competition> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CompetitionDTO convertToDTO(Object entity) {
        Competition competition = (Competition) entity;
        CompetitionDTO dto = modelMapper.map(competition, CompetitionDTO.class);
        return dto;
    }
    public List<CompetitionDTO> convertToList(List<Competition> competitionDTOSs){
        List<CompetitionDTO>  competitionDtos = new ArrayList<>();
        for(Competition c:competitionDTOSs){
            competitionDtos.add(convertToDTO(c));
        }
        return  competitionDtos;
    }

    @Override
    public Competition convertToEntity(Object dto) {
        Competition entity = modelMapper.map(dto, Competition.class);
        return entity;
    }
}
