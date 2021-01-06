package com.vnpost.elearning.processor;

import com.querydsl.jpa.impl.JPAQuery;
import com.vnpost.elearning.converter.CourseWareConverter;
import com.vnpost.elearning.dto.course.CourseWareDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.entities.chapter.ChapterCourseWare;
import eln.common.core.entities.chapter.QChapterCourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 19,2020
 */
@Service
public class ChapterCourseWareProcessor {
  @Autowired private ChapterCourseWareService chapterCourseWareService;
  @Autowired private CourseWareConverter courseWareConverter;
  @Autowired private CourseWareTypeService courseWareTypeService;
  @Autowired private CourseWareService courseWareService;
  @Autowired private ChapterService chapterService;
  @Autowired private CourseWareProcessService courseWareProcessService;
  private final QChapterCourseWare Q = QChapterCourseWare.chapterCourseWare;

  public List<CourseWareDTO> getListCourseWareFromChapterCourseWareSQL(Long id) {
    List<Long> getIdCourseById = chapterCourseWareService.getListCourseIdByChapterId(id);
    List<CourseWareDTO> courseWareDTOList = new ArrayList<>();
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    for (Long i : getIdCourseById) {
      CourseWareDTO courseWareDTOs =
          courseWareConverter.convertToDTO(courseWareService.findById(i).get());
      courseWareDTOs.setTypeCourseWareCode(
          courseWareTypeService.findByCode(courseWareDTOs.getTypeCourseWareCode()).getName());
      setStatusLearning(courseWareDTOs, id);
      courseWareDTOList.add(courseWareDTOs);
      Optional<CourseWareProcess> process = courseWareProcessService.findByUserIdAndCourseWareIdAndChapterId(myUser.getId(),courseWareDTOs.getId(),id);
      courseWareDTOs.setPercentFinished(process.isPresent() ? process.get().getPercentFinished(): 0L);
    }
    return courseWareDTOList;
  }

  private void setStatusLearning(CourseWareDTO courseWareDTOs, Long idChapter) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (courseWareProcessService.exitsUserAndCourseWareAndStatus(
        myUser.getId(), courseWareDTOs.getId(), idChapter, 1)) {
      courseWareDTOs.setStatusComplete(1);
    } else {
      courseWareDTOs.setStatusComplete(0);
    }
  }


  public List<Long> getIdsCourseWareByCourseId(Long courseId) {
    List<Long> chapterList = chapterService.findIdsChapterByCourseId(courseId);
    JPAQuery<ChapterCourseWare> query = new JPAQuery<>(chapterCourseWareService.getEntityManager());
    return query
        .select(Q.courseWare.id)
        .from(Q)
        .orderBy(Q.position.asc())
        .where(Q.chapter.id.in(chapterList))
        .fetch();
  }

  public Integer countByChapterId(Long chapterId){
     return   chapterCourseWareService.countByChapterId(chapterId);
  }

}
