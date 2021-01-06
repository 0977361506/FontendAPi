package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.course.ChapterCourseWareDTO;
import eln.common.core.entities.chapter.ChapterCourseWare;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChapterCourseWareConverter implements IDTO<ChapterCourseWareDTO>, IEntity<ChapterCourseWare> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChapterCourseWareDTO convertToDTO(Object entity) {
        ChapterCourseWare chapterCourseWare = (ChapterCourseWare) entity;
        ChapterCourseWareDTO dto = modelMapper.map(chapterCourseWare, ChapterCourseWareDTO.class);
        return dto;
    }

    @Override
    public ChapterCourseWare convertToEntity(Object dto) {
        ChapterCourseWareDTO chapterCourseWareDTO = (ChapterCourseWareDTO) dto;
        ChapterCourseWare chapterCourseWare = modelMapper.map(chapterCourseWareDTO, ChapterCourseWare.class);
        return chapterCourseWare;
    }
}
