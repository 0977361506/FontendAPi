package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.course.ChapterDTO;
import com.vnpost.elearning.dto.course.CourseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OutlineDTO  {

    private Long id;
    private Long courseId;
    private String name;
    private Long[] listChapterId = new Long[]{};
    private String[] listChapterName= new String[]{};
   private List<ChapterDTO> chapters = new ArrayList<>();

  @JsonIgnore
   private CourseDTO course;



}
