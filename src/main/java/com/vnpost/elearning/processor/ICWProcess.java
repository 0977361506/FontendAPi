package com.vnpost.elearning.processor;

import com.vnpost.elearning.dto.course.CourseProgressDTO;
import eln.common.core.entities.courseware.CourseWare;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 16,2020
 */
// CourseWare Prcesss

public interface ICWProcess {

  default void update(CourseProgressDTO courseProgressDTO) {}

  default void create(CourseWare courseWare, Long chapterId, String username) {}

  String getType();
}
