package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChapterDTO{
    private Long id;
    @JsonIgnore
    private OutlineDTO outline ;

    private Integer status;

    private String name;


    private List<ChapterCourseWareDTO> chapterCourseWares = new ArrayList<>();

    private Integer sequentially;

    private Long competitionId;
    private Long roundId;
    private Integer completeExam;
    private  boolean checkCompleteChapter ;
}
