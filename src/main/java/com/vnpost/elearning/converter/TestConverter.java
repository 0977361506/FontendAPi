package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.TestDTO;
import eln.common.core.entities.competition.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestConverter implements IDTO<TestDTO>, IEntity<Test> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TestDTO convertToDTO(Object entity) {
        TestDTO dto = modelMapper.map(entity, TestDTO.class);
        return dto;
    }

    @Override
    public Test convertToEntity(Object dto) {
        Test entity = modelMapper.map(dto, Test.class);
        return entity;
    }
}
