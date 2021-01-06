package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.course.CourseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CourseConfigDTO {
    private Long courseId;
    private Long id;

    private String end;

    private String start;


    private Integer approveAuto;

    private Date ends;

    private Date starts;


    private Integer freedomRegister;

    private Date registerEnd;


    private Date registerStart;


    private Date startLearning;//

    private Date endLearning;


    @JsonIgnore
    private CourseDTO course;
}
