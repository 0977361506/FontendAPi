package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.GroupTestDTO;
import eln.common.core.entities.GroupTest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupTestConverter implements IEntity<GroupTest>, IDTO<GroupTestDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GroupTestDTO convertToDTO(Object entity) {
        GroupTestDTO dto = modelMapper.map(entity, GroupTestDTO.class);
        return dto;
    }

    @Override
    public GroupTest convertToEntity(Object dto) {
        GroupTest entity = modelMapper.map(dto, GroupTest.class);
        return entity;
    }

}
