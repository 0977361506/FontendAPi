package com.vnpost.elearning.dto.course;

import lombok.Data;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 07,2020
 */
@Data
public class CourseProgressDTO {
  private Long duration;

  private Long idCourseWare;

  private Long idChapter;

  private String userName;

  private Long process;

  private Long processQuitz;
}
