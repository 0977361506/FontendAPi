package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.OutlineDTO;
import eln.common.core.entities.course.Outline;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OutlineConverter implements IDTO<OutlineDTO>, IEntity<Outline> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OutlineDTO convertToDTO(Object entity) {
        OutlineDTO dto = modelMapper.map(entity, OutlineDTO.class);
        return dto;
    }

    @Override
    public Outline convertToEntity(Object dto) {
        OutlineDTO outlineDTO = (OutlineDTO) dto;
        Outline outline = modelMapper.map(outlineDTO, Outline.class);


        return outline;
    }
}
