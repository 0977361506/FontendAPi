package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.RateDTO;
import eln.common.core.entities.course.Rate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RateConverter implements IDTO<RateDTO>, IEntity<Rate> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RateDTO convertToDTO(Object entity) {
        RateDTO dto = modelMapper.map(entity, RateDTO.class);
        return dto;
    }
    public List<RateDTO> convertToList(List<Rate> rates){
        List<RateDTO> lisst = new ArrayList<>();
        for(Rate r : rates){
            lisst.add(convertToDTO(r));
        }
        return  lisst ;
    }
    @Override
    public Rate convertToEntity(Object dto) {
        Rate rate = modelMapper.map(dto, Rate.class);
        return rate;
    }
}
