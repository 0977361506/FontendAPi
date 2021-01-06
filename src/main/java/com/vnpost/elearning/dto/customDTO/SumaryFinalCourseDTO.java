package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.course.ChapterDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SumaryFinalCourseDTO {
    List<ChapterDTO> chapterDTOs;
    StatusExamDTO statusExamDTO;

    public SumaryFinalCourseDTO(List<ChapterDTO> chapterDTOs, StatusExamDTO statusExamDTO) {
        this.chapterDTOs = chapterDTOs;
        this.statusExamDTO = statusExamDTO;
    }
}
