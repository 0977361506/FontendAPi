package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.TestkitDTO;
import eln.common.core.entities.competition.TestKit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestKitConverter implements IDTO<TestkitDTO>, IEntity<TestKit> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TestkitDTO convertToDTO(Object entity) {
        TestkitDTO dto = modelMapper.map(entity, TestkitDTO.class);
        return dto;
    }

    @Override
    public TestKit convertToEntity(Object dto) {
        TestKit entity = modelMapper.map(dto, TestKit.class);
        return entity;
    }
}
