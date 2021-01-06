package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UsingLogDTO {
    private Long id;

    private String userName;

    private Date startTime;

    private Date endTime;

    private Long duration;

    private Long idCourseWare;

    private Long idChapter;

    private Long partDone;

    private Long length;

    private String courseWareType;

    private Long process;

    private Long totalQuitz;

    private Long quitzDone;

    private UsingType type;

    private List<Long> idChapters;

    public enum UsingType {
        LOGIN,
        LEARNING,
        EXAM
    }
}
