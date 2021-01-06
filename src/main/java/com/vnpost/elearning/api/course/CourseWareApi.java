package com.vnpost.elearning.api.course;

import com.vnpost.elearning.dto.course.ChapterDTO;
import com.vnpost.elearning.dto.course.CourseWareDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.ChapterCourseWareProcessor;
import com.vnpost.elearning.processor.ChapterProcessor;
import com.vnpost.elearning.processor.CourseWareProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CandidateService;
import com.vnpost.elearning.service.CompetitionChapterService;
import com.vnpost.elearning.service.CourseService;
import com.vnpost.elearning.service.CourseWareProcessService;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.exception.CourseWareException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 19,2020
 */
@RestController
@RequestMapping("/api")
public class CourseWareApi {
  @Autowired CourseWareProcessService courseWareProcessService;
  @Autowired private CourseWareProcessor courseWareProcessor;
  @Autowired private ChapterProcessor chapterProcessor;
  @Autowired private CourseService courseService;
  @Autowired private ChapterCourseWareProcessor chapterCourseWareProcessor;
  @Autowired private CandidateService candidateService;
  @Autowired private CompetitionChapterService competitionChapterService;


  @GetMapping("/course-ware/course/{courseId}/chapter/{chapterId}")
  public ResponseEntity<ServiceResult> getAllByChapter(
      @PathVariable Long chapterId, @PathVariable Long courseId) {
    ServiceResult serviceResult = new ServiceResult("Danh sách học liệu ", "200");
    try {
      List<CourseWareDTO> listData = courseWareProcessor.findByChapterId(chapterId, courseId);
      serviceResult.setData(listData);
    } catch (CourseWareException e) {
      serviceResult.setCode("500");
      serviceResult.setMessage(e.getMessage());
      return new ResponseEntity(serviceResult, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(serviceResult);
  }

  @GetMapping(
      "/course-ware/course/{id}") // status =1 (đã học xong hết các học liệu) // sequentially= 1
  // (khóa học tuần tự)
  public ResponseEntity<ServiceResult> getAllCourseWare(@PathVariable Long id) {
    ServiceResult serviceResult = new ServiceResult("Danh sách học liệu chương mục", "200");
    try {
      List<ChapterDTO> listData = chapterProcessor.findByCourseId(id);
      checkfinalChapter(listData, id);
      serviceResult.setData(listData);
    } catch (Exception e) {
      serviceResult.setData(e.getMessage());
      serviceResult.setCode("500");
      return new ResponseEntity(serviceResult, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(serviceResult);
  }

  private void checkfinalChapter(List<ChapterDTO> listData, Long idCourse) {
    for (ChapterDTO chapterDTO : listData) {
      for(int i=0 ;i<chapterDTO.getChapterCourseWares().size();i++){
        chapterDTO.getChapterCourseWares().get(i).getCourseWare().setPercentFinished(0L);
        chapterDTO.getChapterCourseWares().get(i).getCourseWare().setStatus(0);
      }
      checkStatus(chapterDTO, idCourse);
    }
  }

  private void checkStatus(ChapterDTO chapterDTO, Long idCourse) {
    Integer stepbyStep = courseService.getstepbystepByIdCorse(idCourse);
    chapterDTO.setSequentially(stepbyStep == null ? 0 : stepbyStep);
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer status = statusChapter(user, chapterDTO, stepbyStep);
    chapterDTO.setStatus((stepbyStep == null) ? null : status);
    chapterDTO.setCompleteExam(checkCompleteExam(chapterDTO.getId(),user.getId()));
  }

  private Integer checkCompleteExam(Long idChapter,Long idUser) {
       Long idround = competitionChapterService.findidRoundByidChapter(idChapter);
       if(idround!=null) {
        return candidateService.countByRoundTestIdAndUserIdAndCounttest(idround, idUser, 1);
       }else{
         return null;
       }
  }

  private Integer statusChapter(MyUser myUsers, ChapterDTO chapterDTO, Integer stepbyStep) {
    List<CourseWareProcess> courseWareProcess =
        courseWareProcessService.findByIduIdChapterId(myUsers.getId(), chapterDTO.getId());
    setStatusForcourseWare(courseWareProcess, chapterDTO,myUsers.getId());
    if (stepbyStep != null) {
      if (stepbyStep == 1 && courseWareProcess.size() > 0) {
        return chapterProcessor.setStatusChapter(chapterDTO,courseWareProcess);
      } else {
        return 0;
      }
    }
    return 0;
  }


  private void setStatusForcourseWare(
      List<CourseWareProcess> courseWareProcess, ChapterDTO chapterDTO,Long idUser) {
    if (courseWareProcess.size() > 0 && chapterDTO.getChapterCourseWares().size() > 0) {
      for (int i = 0; i < chapterDTO.getChapterCourseWares().size(); i++) {
        Integer kt = 0;
        Integer index = null;
        chapterDTO.getChapterCourseWares().get(i).getCourseWare().setStatus(kt);
        for (CourseWareProcess wareProcess : courseWareProcess) {
          if ((chapterDTO.getChapterCourseWares().get(i).getCourseWare().getId().toString())
              .equals(wareProcess.getCourseWare().getId().toString())) {
                kt = 1;
                index = i;
                break;
          }
        }
        if (index != null) {
          chapterDTO.getChapterCourseWares().get(index).getCourseWare().setStatus(kt);
          Optional<CourseWareProcess> process = courseWareProcessService.findByUserIdAndCourseWareIdAndChapterId(idUser
                  ,chapterDTO.getChapterCourseWares().get(index).getCourseWare().getId(),chapterDTO.getId());
          chapterDTO.getChapterCourseWares().get(index).getCourseWare().setPercentFinished(process.isPresent() ? process.get().getPercentFinished(): 0L);
        };
      }
    }
  }
}
