package com.vnpost.elearning.dto.course;

import com.vnpost.elearning.dto.BaseDTO;
import com.vnpost.elearning.dto.course.CourseWareDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChapterCourseWareDTO extends BaseDTO {



    private Long chapterId;

    private Long courseWareId;
    private Long courseId;

    private Integer position;
    private Integer statusProcess;
    private Float percentFinished=0F;


    private Date limitedDate;

//    @JsonIgnore
//    private ChapterDTO chapter;


    private CourseWareDTO courseWare;
}
