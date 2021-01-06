package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.ChapterConverter;
import com.vnpost.elearning.dto.course.ChapterDTO;
import com.vnpost.elearning.dto.customDTO.SumaryFinalCourseDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.chapter.QChapter;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.exception.CourseWareException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 10,2020
 */
@Service
@AllArgsConstructor
public class ChapterProcessor {
  private ChapterService chapterService;
  private ChapterConverter converter;
  private CourseService courseService;
  private CourseJoinService courseJoinService;
  private ChapterCourseWareService chapterCourseWareService;
  private CourseWareProcessService courseWareProcessService;
  private CompetitionChapterService competitionChapterService;
  private RoundTestService roundTestService;
  private final QChapter Q = QChapter.chapter;

  public ChapterDTO findById(Long id) {
    return converter.convertToDTO(chapterService.findById(id));
  }

  public List<ChapterDTO> findByCourseId(Long courseId) throws CourseWareException {
    validCourse(courseId);
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.id.in(chapterService.findIdsChapterByCourseId(courseId)));
    Iterator<Chapter> iterator = chapterService.findAll(builder).iterator();
    List<Chapter> result = new ArrayList<>();
    iterator.forEachRemaining(result::add);
    return result.stream().map(converter::convertToDTO).collect(Collectors.toList());
  }

  private void validCourse(Long courseId) throws CourseWareException {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<Course> course = courseService.findById(courseId);
    if (!course.isPresent()) {
      throw new CourseWareException("Không tìm thấy khóa học");
    }
    if (course.get().getStatus() != SystemConstant.ENABLE) {
      throw new CourseWareException("Khóa học đang đóng");
    }
    //    if (!courseJoinService.existUserInCourse(myUser.getId(), courseId)) {
    //      throw new CourseWareException("Cần có trong khóa học");
    //    }
  }


  public Integer setStatusChapter(ChapterDTO chapterDTO,List<CourseWareProcess> courseWareProcess){
        Integer countCourseWare =
                chapterCourseWareService.countByChapterId(chapterDTO.getId());
        Integer count = 0;
        for (CourseWareProcess wareProcess : courseWareProcess) {
          if (wareProcess.getStatus() == 1) {
            count++;
          }
        }
        if (count >= countCourseWare) {
          return 1;
        } else {
          return 0;
        }
  }

  public   SumaryFinalCourseDTO  getFinalCourseDTOS(Long idCourse){
    List<ChapterDTO> listData = chapterService.findByCourseId(idCourse)
            .stream().map(item->converter.convertToDTO(item)).collect(Collectors.toList());
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    setStatusForListChaper(listData,user);
    return new SumaryFinalCourseDTO(listData,competitionChapterService
            .setStatusExamDTO(roundTestService.getListIdRound(courseService.findByChapterId(listData.get(0).getId()))));
  }


  private void setStatusForListChaper(List<ChapterDTO> listData, MyUser user) {
    for (ChapterDTO chapterDTO:listData){
        chapterDTO.setChapterCourseWares(null);
        List<CourseWareProcess> courseWareProcess =
              courseWareProcessService.findByIduIdChapterId(user.getId(), chapterDTO.getId());
      chapterDTO.setStatus(setStatusChapter(chapterDTO,courseWareProcess));
    }
  }


}
