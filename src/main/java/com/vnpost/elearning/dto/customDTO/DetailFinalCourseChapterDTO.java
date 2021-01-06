package com.vnpost.elearning.dto.customDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DetailFinalCourseChapterDTO implements Serializable {
    private List<CustomCourseProcessDTO> courseWareProcessList;
    private  StatusExamDTO statusExamDTO;


    public DetailFinalCourseChapterDTO(List<CustomCourseProcessDTO> courseWareProcessList, StatusExamDTO statusExamDTO) {
        this.courseWareProcessList = courseWareProcessList;
        this.statusExamDTO = statusExamDTO;
    }
}
