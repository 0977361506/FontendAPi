package com.vnpost.elearning.processor;

import com.google.gson.Gson;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.vnpost.elearning.converter.CourseWareProcessConverter;
import com.vnpost.elearning.dto.course.CourseProgressDTO;
import com.vnpost.elearning.dto.course.CourseWareProcessDTO;
import com.vnpost.elearning.processor.factory.CourseWareProcessFactory;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.courseware.QCourseWareProcess;
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
 *     <p>June 09,2020
 */
@Service
@AllArgsConstructor
public class CourseWareProgressProcessor {
  private CourseWareProcessService courseWareProcessService;
  private CourseWareService courseWareService;
  private ChapterService chapterService;
  private UserService userService;
  private CourseService courseService;
  private MailConfigService mailConfigService;
  private CourseJoinService courseJoinService;
  private ChapterCourseWareService chapterCourseWareService;
  private CourseWareProcessConverter converter;
  private final QCourseWareProcess Q = QCourseWareProcess.courseWareProcess;
  private final Gson gson = new Gson();

  public void saveProcess(Long courseWareId, Long chapterId) throws CourseJoinException {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = myUser.getId();
    Course course = courseService.findByChapterId(chapterId);
    validProcess(course, userId);
    if (courseWareProcessService.exitsUserAndCourseWare(userId, courseWareId, chapterId)) {
      courseWareProcessService.updateView(userId, courseWareId, chapterId);
      return;
    }
    CourseWareProcess courseWareProcess = setCourseWareProcess(userId, courseWareId, chapterId);
    courseWareProcess.setIdcourse(course.getId());
    courseWareProcessService.save(courseWareProcess);
  }

  //  @KafkaListener(topics = "reflectoring-course-progress", groupId = "group-id")
  @Transactional
  public void listenUpdateProgress(String data) {
    CourseProgressDTO courseProgressUpdate = gson.fromJson(data, CourseProgressDTO.class);
    CourseWare courseWare =
        courseWareService.findById(courseProgressUpdate.getIdCourseWare()).get();
    ICWProcess factory =
        CourseWareProcessFactory.getProcess(courseWare.getCourseWareType().getCode());
    factory.update(courseProgressUpdate);
  }

  @Transactional
  public void listenUpdateProgress(CourseProgressDTO courseProgressUpdate) {
    CourseWare courseWare =
        courseWareService.findById(courseProgressUpdate.getIdCourseWare()).get();
    ICWProcess factory =
        CourseWareProcessFactory.getProcess(courseWare.getCourseWareType().getCode());
    factory.update(courseProgressUpdate);
  }

  public List<Long> getListSeenByCourseId(Long courseId) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    JPAQuery<CourseWareProcess> query = new JPAQuery<>(courseWareProcessService.getEntityManager());
    List<Long> listChapterId = chapterService.findIdsChapterByCourseId(courseId);
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.status.eq(SystemConstant.ENABLE));
    builder.and(Q.user.id.eq(myUser.getId()));
    builder.and(Q.chapter.id.in(listChapterId));
    Long countAll = chapterCourseWareService.countAllByListChapterId(listChapterId);
    return query.select(Q.courseWare.id).from(Q).where(builder).fetch();
  }

  private void validProcess(Course course, Long userId) throws CourseJoinException {
    if (course == null) {
      throw new CourseJoinException("Không tìm thấy khóa học");
    }
    if (course.getStatus() == SystemConstant.DISABLE) {
      throw new CourseJoinException("Khóa học đang bị khóa");
    }
    if (!courseJoinService.existUserInCourse(userId, course.getId())) {
      throw new CourseJoinException("Chưa có trong khóa học");
    }
  }

  private CourseWareProcess setCourseWareProcess(Long userId, Long courseWareId, Long chapterId) {
    CourseWareProcess courseWareProcess = new CourseWareProcess();
    courseWareProcess.setDuration(0L);
    courseWareProcess.setUser(userService.findById(userId).get());
    courseWareProcess.setCourseWare(courseWareService.findById(courseWareId).get());
    courseWareProcess.setChapter(chapterService.findById(chapterId).get());
    CourseWare courseWare = courseWareService.findById(courseWareId).get();
    if (courseWare.getCourseWareType().getCode().equals("powerpoint")) {
      courseWareProcess.setStatus(SystemConstant.ENABLE);
    } else {
      courseWareProcess.setStatus(SystemConstant.DISABLE);
    }
    courseWareProcess.setTotalView(1);
    courseWareProcess.setLastView(1);
    return courseWareProcess;
  }

  public Integer countByUserAndCourseWareAndStatus(Long userId, Long chapterId, Integer status) {
    return courseWareProcessService.countByUserAndCourseWareAndStatus(userId, chapterId, status);
  }

  public Integer countByUserAndIdChaptersAndStatus(
      Long userId, List<Long> chapterIds, Integer status) {
    return courseWareProcessService.countByUserAndIdChaptersAndStatus(userId, chapterIds, status);
  }

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

  public void listenCreateProgress(CourseProgressDTO courseProgressDTO) {
    CourseWare courseWare =
            courseWareService.findById(courseProgressDTO.getIdCourseWare()).get();
    ICWProcess factory =
            CourseWareProcessFactory.getProcess(courseWare.getCourseWareType().getCode());
    factory.create(courseWare,courseProgressDTO.getIdChapter(),courseProgressDTO.getUserName());
  }
  //  @KafkaListener(topics = "reflectoring-course-progress", groupId = "group-id")
  public void listenCreateProgress(String data) {
    CourseProgressDTO courseProgressDTO = gson.fromJson(data,CourseProgressDTO.class);
    CourseWare courseWare =
            courseWareService.findById(courseProgressDTO.getIdCourseWare()).get();
    ICWProcess factory =
            CourseWareProcessFactory.getProcess(courseWare.getCourseWareType().getCode());
    factory.create(courseWare,courseProgressDTO.getIdChapter(),courseProgressDTO.getUserName());
  }

  public CourseWareProcessDTO getProcess(Long courseWareId, Long chapterId) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    CourseWareProcess entity = courseWareProcessService.getCourseWareProcess(courseWareId,chapterId,myUser.getId());
    return converter.convertToDTO(entity);
  }


  public Boolean checkFinishCourse(String userName,Long idChapter, Long idCourseWare){
    try {
      User user =   userService.findByUsername(userName);
      Course course = courseService.findByChapterId(idChapter);
      List<Long> chapterIds = chapterService.findListIdByOutlineId(course.getId());
      if(chapterCourseWareService.countTotalCourseWareInCourse(course.getId(),chapterIds)<= courseWareProcessService
              .countByUserAndIdChaptersAndStatus(user.getId(),chapterIds, 1)){
         mailConfigService.sendEmailCourseUnExam(user,course);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }






}
