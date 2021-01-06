package com.vnpost.elearning.processor.factory;

import com.vnpost.elearning.dto.course.CourseProgressDTO;
import com.vnpost.elearning.service.ChapterService;
import com.vnpost.elearning.service.CourseService;
import com.vnpost.elearning.service.CourseWareProcessService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 16,2020
 */
@Component
@AllArgsConstructor
public class CommonProcess {
  private @Autowired CourseWareProcessService courseWareProcessService;
  private @Autowired UserService userService;
  private @Autowired ChapterService chapterService;
  private @Autowired CourseService courseService;

  public CommonProcess() {}

  public CourseWareProcess findByCourseProgress(CourseProgressDTO courseProgress) {
    User user = userService.findByUsername(courseProgress.getUserName());
    Optional<CourseWareProcess> courseWareProcess =
        courseWareProcessService.findByUserIdAndCourseWareIdAndChapterId(
            user.getId(), courseProgress.getIdCourseWare(), courseProgress.getIdChapter());
    if (!courseWareProcess.isPresent()) {
      throw new NullPointerException("Không tìm thấy thông tiến trình");
    }
    return courseWareProcess.get();
  }

  public CourseWareProcess setCourseWareProcess(
      String username, CourseWare courseWare, Long chapterId) {
    CourseWareProcess courseWareProcess = new CourseWareProcess();
    courseWareProcess.setDuration(0L);
    courseWareProcess.setUser(userService.findByUsername(username));
    courseWareProcess.setCourseWare(courseWare);
    courseWareProcess.setChapter(chapterService.findById(chapterId).get());
    courseWareProcess.setStatus(SystemConstant.DISABLE);
    courseWareProcess.setTotalView(0);
    courseWareProcess.setLastView(0);
    Course course = courseService.findByChapterId(chapterId);
    courseWareProcess.setIdcourse(course.getId());
    return courseWareProcess;
  }
}
