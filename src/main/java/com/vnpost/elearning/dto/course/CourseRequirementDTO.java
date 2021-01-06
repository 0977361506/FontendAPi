package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.PositionNameDTO;
import com.vnpost.elearning.dto.course.CourseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequirementDTO {
    private int id;

    @JsonIgnore
    private CourseDTO course;

    @JsonIgnore
    private PositionNameDTO positionName;
}
