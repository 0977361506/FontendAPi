package com.vnpost.elearning.dto;

import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.CourseRoleDTO;
import lombok.Data;

import java.util.Date;

@Data
public class UserPermissDTO {
    private long id;

    private Date lastUpdate;

    private int status;

    private Date timeCreate;


    private CourseDTO course;


    private CourseRoleDTO courseRole;


    private UsersDTO user;
}
