package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.competition.TagDTO;
import eln.common.core.entities.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagsConverter implements IDTO<TagDTO>, IEntity<Tag> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TagDTO convertToDTO(Object entity) {
        TagDTO dto = modelMapper.map(entity, TagDTO.class);
        return dto;
    }

    @Override
    public Tag convertToEntity(Object dto) {
        Tag entity = modelMapper.map(dto, Tag.class);
        return entity;
    }
}
