package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.UsersDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RateDTO extends AbstractDTO<RateDTO> {

    private Integer valuess;

    private UsersDTO user;


    private String star_one;
    private String star_two;
    private String star_three;

    private String star_for;

    private String star_five;

    @JsonIgnore
    private CourseDTO course;
    private Long courseId;
}
