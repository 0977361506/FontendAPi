package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CourseJoinDTO {
    private Long id;
    private Integer status;

    private int join;
    @NotNull
    private Long courseId; //
    @NotBlank
    private String courseCode; //

    private Long userId;

//    @JsonIgnore
//    private UsersDTO user;

    private Long groupId;

    private Integer finished;

    private float percent;
    private String nameStatistic;

}
