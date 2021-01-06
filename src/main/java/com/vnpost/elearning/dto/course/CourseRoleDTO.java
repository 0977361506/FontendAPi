package com.vnpost.elearning.dto.course;

import com.vnpost.elearning.dto.UserRoleCourseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CourseRoleDTO {
    private long id;

    private Date lastUpdate;

    private String nameCourse;

    private Date timeCreate;


    private List<RoleCourseActionDTO> roleCourseActions;


    private List<UserRoleCourseDTO> userRoleCourses;
}
