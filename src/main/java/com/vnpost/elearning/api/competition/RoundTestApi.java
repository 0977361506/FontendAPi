package com.vnpost.elearning.api.competition;

import com.vnpost.elearning.Beans.CheckPermistion;
import com.vnpost.elearning.Beans.CoreConstant;
import com.vnpost.elearning.converter.RoundTestConverter;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.customDTO.CustomQRoundTestDTO;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.processor.CourseWareProgressProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.entities.competition.RoundTest;
import eln.common.core.repository.RoundTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoundTestApi {
  @Autowired private CandidateService candidateService;
  @Autowired private RoundTestService roundTestService;
  @Autowired private RoundTestRepository roundTestRepository;
  @Autowired private ResultService resultService;
  @Autowired private CheckPermistion permistion;
  @Autowired private TimeStartExamService timeStartExamService;
  @Autowired private RoundTestConverter roundTestConverter;
  @Autowired private CourseWareProgressProcessor courseWareProgressProcessor;
  @Autowired private ChapterCourseWareService chapterCourseWareService;
  @Autowired private ChapterService chapterService;
  @Autowired private CourseJoinProcessor courseJoinProcessor;



  @PostMapping("/roundtest/{id_round}/confirm")
  public ResponseEntity<?> confirm(
          @PathVariable("id_round") String id_round,
          @RequestParam(required = false,name = "idChapter")  Long idChapter,
          @RequestParam(required = false,name = "idCourse")  Long idCourse
  ) throws Exception {
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Date date = new Date();
    RoundTest roundTest = roundTestRepository.findById(Long.parseLong(id_round)).get();


    if (roundTest.getStatusRound()==1) {
      return new ResponseEntity<>(new ServiceResult(CoreConstant.EXAM_CLOSE, "Vòng thi chưa được mở", "200"), HttpStatus.OK);
    }
    if(idChapter!=null){
          if(!checkFinalChapter(id_round,idChapter,myUsers)){
            return new ResponseEntity<>(new ServiceResult(CoreConstant.DONT_FINSHED_COURSE_WARE_STUDY, "Bạn chưa học hết các học liệu", "401"), HttpStatus.UNAUTHORIZED);
          }
    }
    if(idCourse!=null){
      if(!checkFinalCourse(id_round,idCourse,myUsers)){
        return new ResponseEntity<>(new ServiceResult(CoreConstant.DONT_FINSHED_CHAPTER_STUDY, "Bạn chưa học xong các chương mục", "401"), HttpStatus.UNAUTHORIZED);
      }
    }
    if (candidateService.countByidUserIdRoundIdCount(myUsers.getId(), roundTest.getId(), "1") > 0) {
      return new ResponseEntity<>(new ServiceResult(CoreConstant.SUCCESS, "Thành công", "200"), HttpStatus.OK);
    }
    if (!permistion.checkDateDuration(roundTest, date)) {
      return new ResponseEntity<>(
          new ServiceResult(CoreConstant.FAIL_TIME_DO_EXAM, "Chưa tới hoặc quá thời gian dự thi", "400"), HttpStatus.OK);
    } else if (candidateService.findByIdUserConfirm(myUsers.getId() + "", id_round, "0") > 0) {
      return new ResponseEntity<>(new ServiceResult(CoreConstant.SUCCESS, "Thàng công", "200"), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new ServiceResult(CoreConstant.UN_JOIN_EXAM, "Chưa tham gia vòng thi", "400"), HttpStatus.OK);
    }
  }

  private boolean checkFinalCourse(String idRound,Long idCourse, MyUser user) {
    try {
      List<Long> chapterIds = chapterService.findListIdByOutlineId(idCourse);

      if(chapterCourseWareService.countTotalCourseWareInCourse(idCourse,chapterIds)<= courseWareProgressProcessor
              .countByUserAndIdChaptersAndStatus(user.getId(),chapterIds, 1)){

        if(candidateService.findByIdUser(user.getId() + "", idRound)==0){
          candidateService.saveCandicates(user.getId(),Long.parseLong(idRound),0);
        }
        return true;
      }
    }catch (Exception e){

    }
    return false;
  }

  private boolean checkFinalChapter(String idRound,Long idChapter,MyUser user) {
    try {
      if(chapterCourseWareService.getListCourseIdByChapterId(idChapter).size()
             <=courseWareProgressProcessor
              .countByUserAndCourseWareAndStatus(user.getId(),idChapter, 1)){
            if(candidateService.findByIdUser(user.getId() + "", idRound)==0){
              candidateService.saveCandicates(user.getId(),Long.parseLong(idRound),0);
            }
           return true;
      }
    }catch (Exception e){
        e.getMessage();
    }
    return false;
  }

  @GetMapping("/roundtest/check-time/{id_round}")
  public Boolean checkTime(@PathVariable("id_round") String id_round) {
    //  MyUser myUsers = (MyUser)
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Date date = new Date();
    RoundTest roundTest = roundTestRepository.findById(Long.parseLong(id_round)).get();
    if (!permistion.checkDateDuration(roundTest, date)) {
      return false;
    } else {
      return true;
    }
  }

  @PostMapping("/roundtest/{id_round}/request")
  public ResponseEntity<?> request(@PathVariable("id_round") String id_round) {
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer count = candidateService.findByIdUser(myUsers.getId() + "", id_round);
    if (count <= 0) {
      if (candidateService.requestUser(myUsers.getId() + "", id_round) == 1) {
        return new ResponseEntity<>(new ServiceResult(1, "Thất bại", "400"), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
            new ServiceResult(0, "Gửi yêu cầu thành công ,vui lòng chờ phê duyệt", "200"),
            HttpStatus.OK);
      }
    } else {
      return new ResponseEntity<>(
          new ServiceResult(0, "Xin chờ quản trị viên phê duyệt", "200"), HttpStatus.OK);
    }
  }

  @PostMapping("/roundtest/{id_round}/inputCode/{code}")
  public ResponseEntity<?> inputCode(
      @PathVariable("id_round") String id_round, @PathVariable("code") String code) {
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer count = candidateService.findByIdUser(myUsers.getId() + "", id_round);
    RoundTestDTO roundTestDTO = roundTestService.findById(Long.parseLong(id_round));
    if (roundTestDTO.getCodeRoundTest().equals(code)) {
      if (count == 0) {
        candidateService.saveCandicates(myUsers.getId(), Long.parseLong(id_round), 0);
      } else {
        candidateService.UpdateConfirmCandicates(myUsers.getId(), Long.parseLong(id_round));
      }
      return new ResponseEntity<>(new ServiceResult(0, "Thành công", "200"), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new ServiceResult(1, "Nhập mã code không đúng", "400"), HttpStatus.OK);
    }
  }

  @PostMapping("/roundtest/submit/{id_round}")
  public ResponseEntity<?> saveResult(
      @PathVariable("id_round") Long id_round,
      @RequestBody List<CustomQRoundTestDTO> customQRoundTestDTOs,
      @RequestParam(required = false,name = "idChapter")  Long idChapter,
      @RequestParam(required = false,name = "idCourse")  Long idCourse) {
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer count = null;
    try {
      if (customQRoundTestDTOs.size() > 0) {
        RoundTest roundTest = roundTestService.findByid(id_round);

        if (roundTest.getStatusRound()==1) {
          return new ResponseEntity<>(
                  new ServiceResult("Vòng thị đã bị khóa", "401"), HttpStatus.UNAUTHORIZED);
        };
        if (!candidateService.isCurrentUserInRoundTest(id_round)) {
          return new ResponseEntity<>(
              new ServiceResult("Bạn chưa có trong danh sách thí sinh", "401"),
              HttpStatus.UNAUTHORIZED);
        };
        if (candidateService.checkLockCandidate(myUsers.getId(), id_round) > 0) {
          return new ResponseEntity<>(
              new ServiceResult("Bạn đã bị khóa", "401"), HttpStatus.UNAUTHORIZED);
        };
        if (!permistion.checkDoAgain(
            roundTest,
            candidateService.findByIdUserByIgnoreCount0(myUsers.getId() + "", id_round + "") + 1)) {
          return new ResponseEntity<>(
              new ServiceResult("Bạn đã làm quá số lần", "401"), HttpStatus.UNAUTHORIZED);
        };

        Integer countTest = saveCountTest(myUsers, id_round);
        count = countTest;

        resultService.saveResultSQL(myUsers.getId(), id_round, customQRoundTestDTOs, countTest,idCourse);
        if(idChapter!=null){
            courseJoinProcessor.updateProgress(idChapter,myUsers.getUsername());
        }
        return new ResponseEntity<>(new ServiceResult("Lưu thành công", "200"), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
            new ServiceResult("Lưu thất bại", "500"), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (Exception e) {
      candidateService.deleteByError(myUsers.getId(), id_round, count);
      return new ResponseEntity<>(
          new ServiceResult("Lưu thất bại", "500"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private Integer saveCountTest(MyUser user, Long idRound) {
    Integer count = candidateService.countByidUserIdRoundIdCount(user.getId(), idRound, "0");
    if (count == 1) {
      Date timeStart = timeStartExamService.findByidRoundidUserCount(idRound, user.getId(), count);
      candidateService.updateCountTest(
          timeStart == null ? new Date() : timeStart, user.getId(), idRound, "1");
      return 1;
    } else {
      Integer countIgnorCounttest = candidateService.findByIdUser(user.getId() + "", idRound + "");
      Date timeStart =
          timeStartExamService.findByidRoundidUserCount(
              idRound, user.getId(), countIgnorCounttest + 1);
      candidateService.saveCandicates(user.getId(), idRound, countIgnorCounttest + 1);
      candidateService.updateTimeStart(
          timeStart == null ? new Date() : timeStart,
          user.getId(),
          idRound,
          countIgnorCounttest + 1);
      return countIgnorCounttest + 1;
    }
  }

  @GetMapping("/roundtest/{id}")
  public ResponseEntity<?> findById(@PathVariable("id") Long id) {
    RoundTestDTO roundTestDTO = roundTestConverter.convertToDTO(roundTestService.findById(id).get());
    return new ResponseEntity<>(new ServiceResult(roundTestDTO, "success", "200"), HttpStatus.OK);
  }

  @GetMapping("/roundtest/question/{id_user}/{countTime}/{id_round_test}/list")
  public ResponseEntity<ServiceResult> getListQuestionByidUserCountIdRound(
      @PathVariable(name = "id_user") Long id_user,
      @PathVariable(name = "countTime") String countTime,
      @PathVariable(name = "id_round_test") Long id_round_test) {
    RoundTestDTO roundTest = roundTestConverter.convertToDTO(roundTestService.findById(id_round_test).get());
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (candidateService.countByidUserIdRound(myUsers.getId(), id_round_test) == 0) {
       return new ResponseEntity<>(
               new ServiceResult(null, "Bạn không có trong danh sách", "200"), HttpStatus.OK);
    }
    if (roundTest.getShowResutl() == 0) {
      return new ResponseEntity<>(
          new ServiceResult(
              resultService.getListQuestionRoundTest(myUsers.getId(), countTime, id_round_test),
              "success",
              "200"),
          HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          new ServiceResult(null, "Không cho hiển thị kết quả", "200"), HttpStatus.OK);
    }
  }


  
}
