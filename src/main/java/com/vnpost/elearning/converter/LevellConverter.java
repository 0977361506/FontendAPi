package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.LevellDTO;
import eln.common.core.entities.question.Levell;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LevellConverter implements IDTO<LevellDTO>, IEntity<Levell> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LevellDTO convertToDTO(Object entity) {
        LevellDTO dto = modelMapper.map(entity, LevellDTO.class);
        return dto;
    }

    @Override
    public Levell convertToEntity(Object dto) {
        Levell entity = modelMapper.map(dto, Levell.class);
        return entity;
    }


}
