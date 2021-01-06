package com.vnpost.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.CourseJoinDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class GroupDTO {
    private long id;

    private String createdBy;

    private Date createdDate;

    private String modifiedBy;

    private Date modifiedDate;

    private String name;

    //bi-directional many-to-one association to CourseJoin
    @JsonIgnore
    private List<CourseJoinDTO> courseJoins;

    @JsonIgnore
    private CourseDTO course;
}
