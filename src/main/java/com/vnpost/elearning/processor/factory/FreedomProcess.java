package com.vnpost.elearning.processor.factory;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.vnpost.elearning.dto.course.CourseProgressDTO;
import com.vnpost.elearning.processor.ICWProcess;
import com.vnpost.elearning.service.CourseWareProcessService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.courseware.QCourseWareProcess;
import eln.common.core.entities.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 18,2020
 */
@AllArgsConstructor
@Component
public class FreedomProcess extends CommonProcess implements ICWProcess {
  private CourseWareProcessService courseWareProcessService;
  private UserService userService;
  private final QCourseWareProcess Q = QCourseWareProcess.courseWareProcess;

  @Transactional
  @Override
  public void update(CourseProgressDTO courseProgressUpdate) {
    CourseWareProcess courseWareProcess = findByCourseProgress(courseProgressUpdate);
    JPAUpdateClause update = new JPAUpdateClause(courseWareProcessService.getEntityManager(), Q);
    update.set(Q.status, SystemConstant.ENABLE);
    update.set(Q.percentFinished, 100L);
    update.set(Q.totalView, courseWareProcess.getTotalView() + 1);
    update.where(Q.id.eq(courseWareProcess.getId()));
    update.execute();
  }

  @Override
  public void create(CourseWare courseWare, Long chapterId, String username) {
    User user = userService.findByUsername(username);
    if (courseWareProcessService.exitsUserAndCourseWare(
        user.getId(), courseWare.getId(), chapterId)) {
      return;
    }
    CourseWareProcess courseWareProcess = setCourseWareProcess(username, courseWare, chapterId);
    courseWareProcess.setStatus(SystemConstant.ENABLE);
    courseWareProcessService.save(courseWareProcess);
  }

  @Override
  public String getType() {
    return "freedomlecture";
  }
}
