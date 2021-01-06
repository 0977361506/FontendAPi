package com.vnpost.elearning.dto.course;

import com.vnpost.elearning.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RegisterCourseDTO extends BaseDTO {

    private Long idUser;
    private Long idCourse;
    private String content;
    private Date timeRegister;

    private CourseDTO courseDTO;
    private List<String> categoryNames;
}
