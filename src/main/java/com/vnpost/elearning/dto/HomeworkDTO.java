package com.vnpost.elearning.dto;

import com.vnpost.elearning.dto.course.CourseWareDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class HomeworkDTO {

    private long id;

    private String contents;


    private String description;

    private Date ends;

    private String files;


    private String name;

    private int shared;

    private Date starts;

    private int status;

    private int stored;





    private CourseWareDTO courseWare;



}
