package com.vnpost.elearning.processor.factory;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.vnpost.elearning.dto.course.CourseProgressDTO;
import com.vnpost.elearning.processor.ICWProcess;
import com.vnpost.elearning.service.ChapterCourseWareService;
import com.vnpost.elearning.service.CourseWareProcessService;
import com.vnpost.elearning.service.CourseWareService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.entities.chapter.ChapterCourseWare;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.courseware.QCourseWareProcess;
import eln.common.core.entities.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 16,2020
 */
@Component
@AllArgsConstructor
public class ScormProcess extends CommonProcess implements ICWProcess {
  private CourseWareProcessService courseWareProcessService;
  private UserService userService;
  private CourseWareService courseWareService;
  private ChapterCourseWareService chapterCourseWareService;
  private final QCourseWareProcess Q = QCourseWareProcess.courseWareProcess;

  @Transactional
  @Override
  public void update(CourseProgressDTO courseProgressUpdate) {
    Long process = courseProgressUpdate.getProcess();
    int statusFinish = 0;
    CourseWare courseWare =
        courseWareService.findById(courseProgressUpdate.getIdCourseWare()).get();
    Float percentFinishChapter = 100f;
    ChapterCourseWare chapterCourseWare =
        chapterCourseWareService.findByChapterIdAndCourseWareId(
            courseProgressUpdate.getIdChapter(), courseWare.getId());
    if (chapterCourseWare.getPercentFinished() != null) {
      percentFinishChapter = chapterCourseWare.getPercentFinished();
    }
    CourseWareProcess courseWareProcess = findByCourseProgress(courseProgressUpdate);
    Long totalDuration = courseWareProcess.getDuration();
    if (courseProgressUpdate.getDuration() != null) {
      totalDuration += courseProgressUpdate.getDuration();
    }
    if (courseWareProcess.getPercentFinished() >= process) {
      process = courseWareProcess.getPercentFinished();
    }
    if (process >= percentFinishChapter) {
      statusFinish = 1;
    }
    if (process >= 100) {
      process = 100L;
    }
    JPAUpdateClause update = new JPAUpdateClause(courseWareProcessService.getEntityManager(), Q);
    update.set(Q.duration, totalDuration);
    update.set(Q.status, statusFinish);
    update.set(Q.totalView, courseWareProcess.getTotalView() + 1);
    update.set(Q.percentFinished, process);
    update.where(Q.id.eq(courseWareProcess.getId())).execute();
  }

  @Override
  public void create(CourseWare courseWare, Long chapterId, String username) {
    User user = userService.findByUsername(username);
    if (courseWareProcessService.exitsUserAndCourseWare(
        user.getId(), courseWare.getId(), chapterId)) {
      return;
    }
    CourseWareProcess courseWareProcess = setCourseWareProcess(username, courseWare, chapterId);
    courseWareProcessService.save(courseWareProcess);
  }

  @Override
  public String getType() {
    return "scorm";
  }
}
