package com.vnpost.elearning.dto.course;

import com.vnpost.elearning.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProposeCourseDTO extends BaseDTO {


    private Long id;
    private Long idUser;
    private Long idCourseCategory;
    private String content;
    private Date timeRegister;

    private List<String> categoryNames;
    private CoursecategoryDTO coursecategoryDTO;



}
