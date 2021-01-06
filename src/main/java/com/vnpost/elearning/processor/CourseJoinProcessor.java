package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.CourseJoinDTO;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.chapter.ChapterCourseWare;
import eln.common.core.entities.competition.CompetitionChapter;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.CourseJoin;
import eln.common.core.entities.course.QCourseJoin;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.user.User;
import eln.common.core.exception.CourseJoinException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 04,2020
 */
@Service
@AllArgsConstructor
public class CourseJoinProcessor {
  private CourseJoinService courseJoinService;
  private CourseJoinRequestService courseJoinRequestService;
  private CourseService courseService;
  private UserService userService;
  private ChatRoomService chatRoomService;
  private ChapterService chapterService;
  private CourseWareProcessService courseWareProcessService;
  private ChapterCourseWareService chapterCourseWareService;
  private CompetitionChapterService competitionChapterService;
  private RoundTestService roundTestService;
  private CandidateService candidateService;
  private final QCourseJoin Q = QCourseJoin.courseJoin;

  public void joinWithCode(CourseJoinDTO courseJoinDTO) throws CourseJoinException {
    validJoinWithCode(courseJoinDTO);
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userService.findByUsername(myUser.getUsername());
    courseJoinRequestService.deleteByUserIdAndCourseId(user.getId(), courseJoinDTO.getCourseId());
    Course course = courseService.findById(courseJoinDTO.getCourseId()).get();
    CourseJoin courseJoin = setCourseJoin(user, course);
    courseJoinService.save(courseJoin);
  }

  public Boolean exitsUserAndCourse(Long userId, Long courseId) {
    return courseJoinService.existUserInCourse(userId, courseId);
  }

  public Long totalUserInCourse(Long courseId) {
    BooleanBuilder builder = new BooleanBuilder().and(Q.course.id.eq(courseId));
    return courseJoinService.count(builder);
  }

  @Transactional
  public void updateProcess(Long chapterId) {
    updateProgress(chapterId);
    //    Course course = courseService.findByChapterId(chapterId);
    //    List<Long> chapterIdList = chapterService.findIdsChapterByCourseId(course.getId());
    //    MyUser myUser = (MyUser)
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //    Long totalProgress =
    //        courseWareProcessService.countByChapterIdsAndUserId(chapterIdList, myUser.getId());
    //    Long totalCoursWare = chapterCourseWareService.countAllByListChapterId(chapterIdList);
    //    if (course.getCompetitionId() != null) {
    //      totalCoursWare += 1;
    //    }
    //    float process = 0f;
    //    if (totalCoursWare != 0) {
    //      process = totalProgress / (float) totalCoursWare * 100;
    //    }
    //
    //    CourseJoin coursejoin =
    //        courseJoinService.findByCourseIdAndUserId(course.getId(), myUser.getId());
    //    JPAUpdateClause update = new JPAUpdateClause(courseJoinService.getEntityManager(), Q);
    //    if (process >= 100f) {
    //      process = 100f;
    //      update.set(Q.finished, SystemConstant.ENABLE);
    //    }
    //    update.set(Q.progress, process);
    //    update.set(Q.join, SystemConstant.ENABLE);
    //    update.where(Q.id.eq(coursejoin.getId()));
    //    update.execute();
  }

  @Transactional
  public void updateProgress(Long chapterId) {
    Course course = courseService.findByChapterId(chapterId);
    List<Chapter> listChapter = chapterService.findByCourseId(course.getId());
    int totalChapter = listChapter.size();

    int chapterSuccess = 0;
    for (Chapter chapter : listChapter) {
      int percentFinish = 100;
      List<ChapterCourseWare> listChapterCourseWare =
          chapterCourseWareService.findByChapterId(chapter.getId());
      int totalCourseWare = listChapterCourseWare.size();
      Optional<CompetitionChapter> optionalCompetitionChapter =
          competitionChapterService.findByChapterId(chapter.getId());
      int totcalCourseWarePass = 0;
      MyUser myUser =
          (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (optionalCompetitionChapter.isPresent()) {
        totalCourseWare++;
        CompetitionChapter competitionChapter = optionalCompetitionChapter.get();
        List<RoundTestDTO> roundTestDTOList =
            roundTestService.findByIdCompetition(competitionChapter.getCompetitionId());
        Integer count =
            candidateService.countByRoundTestIdAndUserIdAndCounttest(
                roundTestDTOList.get(0).getId(), myUser.getId(), 1);
        if (count == 1) {
          totcalCourseWarePass++;
        }
      }
      if (totalCourseWare == 0) {
        chapterSuccess++;
        continue;
      }

      for (ChapterCourseWare chapterCourseWare : listChapterCourseWare) {
        float percentFinishChapterCourseWare = 100f;
        if (chapterCourseWare.getPercentFinished() != null) {
          percentFinishChapterCourseWare = chapterCourseWare.getPercentFinished();
        }
        if (percentFinishChapterCourseWare == 0f) {
          totcalCourseWarePass++;
          continue;
        }
        Optional<CourseWareProcess> courseWareProcessOptional =
            courseWareProcessService.findByChapterCourseWareAndUser(
                chapterCourseWare, myUser.getId());
        if (!courseWareProcessOptional.isPresent()) {
          continue;
        }
        CourseWareProcess courseWareProcess = courseWareProcessOptional.get();
        if (courseWareProcess.getStatus() == SystemConstant.FINISHED) {
          totcalCourseWarePass++;
        }
      }

      float percent = totcalCourseWarePass / (float) totalCourseWare * 100;
      if (percent >= percentFinish) {
        chapterSuccess++;
      }
    }
    float process = 0f;
    if (course.getCompetitionId() != null) {
      totalChapter++;
    }
    if (totalChapter != 0) {
      process = chapterSuccess / (float) totalChapter * 100;
    }
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    CourseJoin courseJoin =
        courseJoinService.findByCourseIdAndUserId(course.getId(), myUser.getId());
    JPAUpdateClause update = new JPAUpdateClause(courseJoinService.getEntityManager(), Q);
    if (process >= 100f) {
      process = 100f;
      if (course.getCompetitionId() == null) {
        update.set(Q.finished, SystemConstant.ENABLE);
      }
    }
    update.set(Q.progress, process);
    update.set(Q.join, SystemConstant.ENABLE);
    update.where(Q.id.eq(courseJoin.getId()));
    update.execute();
  }

  @Transactional
  public void updateProgress(Long chapterId, String username) {
    Course course = courseService.findByChapterId(chapterId);
    List<Chapter> listChapter = chapterService.findByCourseId(course.getId());
    int totalChapter = listChapter.size();
    if (course.getCompetitionId() != null) {
      totalChapter++;
    }
    int chapterSuccess = 0;
    User myUser = userService.findByUsername(username);
    for (Chapter chapter : listChapter) {
      int percentFinish = 100;
      int totcalCourseWarePass = 0;
      List<ChapterCourseWare> listChapterCourseWare =
          chapterCourseWareService.findByChapterId(chapter.getId());
      int totalCourseWare = listChapterCourseWare.size();
      Optional<CompetitionChapter> optionalCompetitionChapter =
          competitionChapterService.findByChapterId(chapter.getId());
      if (optionalCompetitionChapter.isPresent()) {
        totalCourseWare++;
        CompetitionChapter competitionChapter = optionalCompetitionChapter.get();
        List<RoundTestDTO> roundTestDTOList =
            roundTestService.findByIdCompetition(competitionChapter.getCompetitionId());
        Integer count =
            candidateService.countByRoundTestIdAndUserIdAndCounttest(
                roundTestDTOList.get(0).getId(), myUser.getId(), 1);
        if (count == 1) {
          totcalCourseWarePass++;
        }
      }

      if (totalCourseWare == 0) {
        chapterSuccess++;
        continue;
      }

      for (ChapterCourseWare chapterCourseWare : listChapterCourseWare) {
        float percentFinishChapterCourseWare = 100f;
        if (chapterCourseWare.getPercentFinished() != null) {
          percentFinishChapterCourseWare = chapterCourseWare.getPercentFinished();
        }
        if (percentFinishChapterCourseWare == 0f) {
          totcalCourseWarePass++;
          continue;
        }
        Optional<CourseWareProcess> courseWareProcessOptional =
            courseWareProcessService.findByChapterCourseWareAndUser(
                chapterCourseWare, myUser.getId());
        if (!courseWareProcessOptional.isPresent()) {
          continue;
        }
        CourseWareProcess courseWareProcess = courseWareProcessOptional.get();
        if (courseWareProcess.getStatus() == SystemConstant.FINISHED) {
          totcalCourseWarePass++;
        }
      }

      float percent = totcalCourseWarePass / (float) totalCourseWare * 100;
      if (percent >= percentFinish) {
        chapterSuccess++;
      }
    }
    float process = 0f;
    if (totalChapter != 0) {
      process = chapterSuccess / (float) totalChapter * 100;
    }
    CourseJoin courseJoin =
        courseJoinService.findByCourseIdAndUserId(course.getId(), myUser.getId());
    JPAUpdateClause update = new JPAUpdateClause(courseJoinService.getEntityManager(), Q);
    if (process >= 100f) {
      process = 100f;
      if (course.getCompetitionId() == null) {
        update.set(Q.finished, SystemConstant.ENABLE);
      }
    }
    update.set(Q.progress, process);
    update.set(Q.join, SystemConstant.ENABLE);
    update.where(Q.id.eq(courseJoin.getId()));
    update.execute();
  }

  private CourseJoin setCourseJoin(User user, Course course) {
    CourseJoin courseJoin = new CourseJoin();
    courseJoin.setCourse(course);
    courseJoin.setUser(user);
    courseJoin.setFinished(SystemConstant.UNFINISHED);
    courseJoin.setStatus(SystemConstant.ENABLE);
    courseJoin.setJoin(SystemConstant.ENABLE);
    courseJoin.setProgress(0f);
    return courseJoin;
  }

  public void joinFreeCourse(CourseDTO courseDTO) throws Exception {
    MyUser curUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (curUser.getId() == null) {
      throw new Exception("You are not login");
    }
    if (courseDTO.getId() == null) {
      throw new Exception("No coures Id");
    }
    Optional<Course> courseOptional = courseService.findById(courseDTO.getId());
    if (!courseOptional.isPresent()) {
      throw new Exception("Course doesn't exit");
    }
    Course courseEntity = courseOptional.get();
    if (courseEntity.getCourseConfig().getFreedomRegister() == 1)
      throw new Exception("This Course doesn't free to register");
    if (courseJoinService.existUserInCourse(curUser.getId(), courseDTO.getId()))
      throw new Exception("You are in course");
    courseJoinRequestService.deleteByUserIdAndCourseId(curUser.getId(), courseEntity.getId());
    CourseJoin courseJoin =
        setCourseJoin(userService.findByUsername(curUser.getUsername()), courseEntity);
    courseJoinService.save(courseJoin);
  }

  private void validJoinWithCode(CourseJoinDTO courseJoinDTO) throws CourseJoinException {
    Optional<Course> courseOptional = courseService.findById(courseJoinDTO.getCourseId());
    if (!courseOptional.isPresent()) {
      throw new CourseJoinException("Không tìm thấy khóa học");
    }
    Course course = courseOptional.get();
    if (course.getStatus() == SystemConstant.DISABLE) {
      throw new CourseJoinException("Khóa học đã khóa");
    }
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (courseJoinService.existUserInCourse(myUser.getId(), course.getId())) {
      throw new CourseJoinException("Đã có trong khóa học");
    }
    if (!course.getCode().equals(courseJoinDTO.getCourseCode())) {
      throw new CourseJoinException("Mã khóa học không chính xác !");
    }
  }
}
