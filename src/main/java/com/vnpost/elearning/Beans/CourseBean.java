package com.vnpost.elearning.Beans;


import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class CourseBean {

    private Long iduser;
    private Long idchapter;
    private Long idcw;
    private  Long idCourse ;
    public CourseBean(Long iduser, Long idchapter, Long idcw, Long idCourse) {
        super();
        this.iduser = iduser;
        this.idchapter = idchapter;
        this.idcw = idcw;
        this.idCourse=idCourse;
    }
    public CourseBean() {
        super();
    }
}
