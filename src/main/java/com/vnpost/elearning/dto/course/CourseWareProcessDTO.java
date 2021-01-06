package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.UsersDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseWareProcessDTO extends AbstractDTO<CourseWareProcessDTO> {

  private Integer status;

  private String comments;

  private String contents;

  private Integer lastView;

  private Long chapterId;

  private Long courseId;

  private UsersDTO user;
  @JsonIgnore private ChapterDTO chapter;

  private CourseWareDTO courseWare;
  private Boolean isFinish;
  private Long duration = 0L;
  private Long percentFinished = 0L;
}
