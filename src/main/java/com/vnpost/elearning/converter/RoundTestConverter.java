package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.RoundTestDTO;
import eln.common.core.entities.competition.RoundTest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class RoundTestConverter implements IDTO<RoundTestDTO>, IEntity<RoundTest> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoundTestDTO convertToDTO(Object entity) {
        RoundTestDTO dto = modelMapper.map(entity, RoundTestDTO.class);
        return dto;
    }
    public List<RoundTestDTO> convertToList(List<RoundTest> tests){
        List<RoundTestDTO> list = new ArrayList<>();
        for(RoundTest d: tests){
            list.add(convertToDTO(d));

        }
        return  list ;
    }
    @Override
    public RoundTest convertToEntity(Object dto) {
        RoundTest entity = modelMapper.map(dto, RoundTest.class);
        return entity;
    }
}
