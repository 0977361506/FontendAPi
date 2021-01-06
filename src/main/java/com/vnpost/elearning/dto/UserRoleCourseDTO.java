package com.vnpost.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.course.CourseRoleDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRoleCourseDTO extends AbstractDTO<UserRoleCourseDTO> {

    private Long groupId;
    private Long courseId;
    private Long roleId;
    private Long userId;
    private Integer score;
    private Date lastUpdate;
    private String evaluate;

    private int status;
    private  UsersDTO user;
    private Date timeCreate;



    @JsonIgnore
    private CourseRoleDTO courseRole;


}
