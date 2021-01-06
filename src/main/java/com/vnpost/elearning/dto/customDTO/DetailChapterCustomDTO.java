package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.course.CourseWareDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DetailChapterCustomDTO implements Serializable {
    private  List<CourseWareDTO> courseWares;
    private  Long idRound;
    private int percent ;

    public DetailChapterCustomDTO(List<CourseWareDTO> courseWares, Long idRound , int percent) {
        this.courseWares = courseWares;
        this.idRound = idRound;
        this.percent = percent ;
    }
}
