package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.TagsConverter;
import com.vnpost.elearning.dto.competition.TagDTO;
import eln.common.core.entities.Tag;
import eln.common.core.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagsService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagsConverter tagsConverter;


    public List<TagDTO> getListByPropertyLike(String name) {
        List<Tag> list = tagRepository.getListByPropertyLike(name);
        List<TagDTO> dtos = new ArrayList<>();
        for (Tag tag : list) {
            TagDTO toDTO = tagsConverter.convertToDTO(tag);
            dtos.add(toDTO);
        }
        return dtos;
    }


    public List<TagDTO> getListByLimit() {
        List<Tag> list = tagRepository.getListByLimit();
        List<TagDTO> dtos = new ArrayList<>();
        for (Tag tag : list) {
            TagDTO toDTO = tagsConverter.convertToDTO(tag);
            dtos.add(toDTO);
        }
        return dtos;
    }


}
