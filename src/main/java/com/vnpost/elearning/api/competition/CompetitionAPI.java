package com.vnpost.elearning.api.competition;

import com.vnpost.elearning.Beans.CheckPermistion;
import com.vnpost.elearning.converter.*;
import com.vnpost.elearning.dto.*;
import com.vnpost.elearning.dto.competition.CompetitionDTO;
import com.vnpost.elearning.processor.CompetitionProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.entities.competition.Candidate;
import eln.common.core.entities.competition.Competition;
import eln.common.core.entities.competition.RoundTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CompetitionAPI {
  @Autowired
  private CandidateConverter candidateConverter;
  @Autowired
  private RoundTestConverter roundTestConverter;
  @Autowired
  private RoundTestService roundTestService;
  @Autowired
  private CompetitionService competitionService;
  @Autowired
  private CompetitionCategoryService cate;
  @Autowired
  private CompetitionConverter competitionConverter;
  @Autowired
  private CandidateService candidateService;
  @Autowired
  private CheckPermistion permistion;
  @Autowired
  private CompetitionProcessor competitionProcessor;


  @PostMapping("/competition/{id_competition}/getPoscodes")
  public ResponseEntity<?> getUnit(@PathVariable("id_competition") Long idCompetition) {
    Object[] objects = competitionProcessor.getUnitConnect(idCompetition);
    if(objects.length>0){
      return new ResponseEntity<>(new ServiceResult(objects, "Thành công", "200"), HttpStatus.OK);
    }else{
      return new ResponseEntity<>(
              new ServiceResult(null, "Thất bại", "500"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

  @GetMapping("/competition/contentTest/{ma}")
  public ResponseEntity<?> contentTesst(@PathVariable("ma") Long id) {
    Date date = new Date();
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer countTest = saveCountTest(user,id);

    RoundTest roundTest = roundTestService.findByid(id);
    if (roundTest.getStatusRound()==1) {
      return  new ResponseEntity<>(new ServiceResult(null,"Vòng thi chưa được mở","401") , HttpStatus.UNAUTHORIZED);
    }
    if (candidateService.checkLockCandidate(user.getId(), roundTest.getId()) > 0) {
      return  new ResponseEntity<>(new ServiceResult(null,"Bạn bị khóa thi","401") , HttpStatus.UNAUTHORIZED);
    }
    if (candidateService.countByidUserIdRound(user.getId(), roundTest.getId())== 0) {
      return  new ResponseEntity<>(new ServiceResult(null,"Bạn không có trong danh sách thí sinh","401") , HttpStatus.UNAUTHORIZED);
    }
    if (!permistion.checkAll(roundTest, date, countTest)) {
      return  new ResponseEntity<>(new ServiceResult(null,"Bạn vượt quá số lần hoặc quá thời gian thi","401") , HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(new ServiceResult(competitionProcessor.contentTesst(id,roundTest,user,countTest,date),"Thành công","200"), HttpStatus.OK);
  }




  private Integer saveCountTest(MyUser user, Long idRound) {
    Integer count = candidateService.countByidUserIdRoundIdCount(user.getId(), idRound, "0");
    if (count == 1) {
      return 1;
    } else {
      Integer countIgnorCounttest = candidateService.findByIdUser(user.getId() + "", idRound + "");
      return countIgnorCounttest + 1;
    }
  }


  @PostMapping("/competition/home")
  public ResponseEntity<?> getListCompetitionsForHomeCompetition(
      @RequestBody CompetitionDTO competitionDTO) {
    try {
      Object[] objectsCompetition = competitionService.getListCompetitions(competitionDTO);
      Object[] objects = {(List<CompetitionDTO>) objectsCompetition[0],cate.findParentOn(competitionDTO), Integer.parseInt(objectsCompetition[1].toString())};
      return new ResponseEntity<>(objects, HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.OK);
    }
  }


  @RequestMapping("/competition/listTest/index/{id}")
  public  ServiceResult ShowListTest(Model model, @PathVariable("id") Long id) {
    return new ServiceResult(roundTestService.findByIdCompetition(id), "success", "200");
  }

  @PostMapping("/competition/home/hight-light")
  public ResponseEntity<?> showHightLight() {
    List<Competition> competitions = competitionService.getHightLight();
    return new ResponseEntity<>(competitionConverter.convertToList(competitions), HttpStatus.OK);
  }

  @GetMapping("/competition/roundtest/{id}")
  public ResponseEntity<Object[]> roundtesst(@PathVariable("id") Long id) {

    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(candidateService.countByidUserIdRound(user.getId(),id)==0){
      return null;
    }

    RoundTest roundTest = roundTestService.findByid(id);
    List<Candidate> list =
        candidateService.getListByIdUser(user.getId(), id); // list candicate theo usser và id rounftest
    int count = candidateService.countByIdusser(user.getId(), id);

    Integer checkLockCandidates =
        candidateService.checkLockCandidate(user.getId(), roundTest.getId());

    Object[] objects = {
      roundTestConverter.convertToDTO(roundTest),
      count,
      id,
      null,
      candidateConverter.convertToList(list),
      user.getId(),
      roundTest.getMaxWork(),
      checkLockCandidates
    };
    return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
  }

  @GetMapping("/competition/roundtest/history/list/{id}")
  public ResponseEntity<ServiceResult> getRoundTestHistoryList(@PathVariable Long id) {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Candidate> list = candidateService.getListByIdUser(user.getId(), id);
    if(list.size()>0){
      if (list.get(0).getCounttest()==0){
        return ResponseEntity.ok(new ServiceResult(null,"Không có lịch sử","200"));
      }else{
        return ResponseEntity.ok(new ServiceResult(candidateConverter.convertToList(list),"Thành công","200"));
      }
    }
    return ResponseEntity.ok(new ServiceResult(null,"Không có lịch sử","200"));
  }



  @GetMapping("/competition/all")
  public ResponseEntity<ServiceResult> allCompetitionNotInCourse() {
    ServiceResult serviceResult = new ServiceResult();
    serviceResult.setData(competitionService.findAllActiveCompetition());
    serviceResult.setMessage("success");
    serviceResult.setCode("200");
    return ResponseEntity.ok(serviceResult);
  }

  @PostMapping("/competition/my-competition")
  public ResponseEntity<?> getMyCompetition(@RequestBody CompetitionDTO competitionDTO) {
    Object[] objectsCompetition = competitionService.getListCompetitionMyCompe(competitionDTO);
    Object[] objects = {
      (List<CompetitionDTO>) objectsCompetition[0],
      Integer.parseInt(objectsCompetition[1].toString())
    };
    return new ResponseEntity<>(new ServiceResult(objects,"Thành công","200"), HttpStatus.OK);
  }




  @GetMapping("/competition/join/{id}/currentUser")
  public ResponseEntity<ServiceResult> checkCurrentUserInRoundTest(@PathVariable Long id) {
    ServiceResult result = new ServiceResult();
    try {
      result.setCode("200");
      result.setMessage(String.valueOf(candidateService.isCurrentUserInRoundTest(id)));
      result.setData(candidateService.isCurrentUserInRoundTest(id));
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      e.printStackTrace();
      result.setCode("400");
      result.setMessage(e.getMessage());
      return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/competition/{id}")
  public ResponseEntity<ServiceResult> getCompetitionById(@PathVariable(name = "id") Long idCompetition) {
    return new ResponseEntity<>(new ServiceResult(competitionService.getDetailCompetition(idCompetition),"success","200"),HttpStatus.OK);
  }

  @GetMapping("/competition/{id}/roundTest/list")
  public ResponseEntity<ServiceResult> getListRoundTestByCompetitionId(@PathVariable Long id) {
    ServiceResult result = new ServiceResult();
    try {
      result.setData(roundTestService.findAllRoundTestByCompetitionId(id));
      result.setCode("200");
      result.setMessage("success");
    } catch (Exception e) {
      result.setCode("500");
      result.setMessage(e.getMessage());
      return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(result);
  }



    @GetMapping("/competition/history/list")
    public ResponseEntity<?> getHistoryCompetition() {
      return new ResponseEntity<>(new ServiceResult(competitionService.getListCompetitionHisCompe(),"Thành công","200"), HttpStatus.OK);
    }


  @GetMapping("/competition/my-competition/statistical")
  public ResponseEntity<ServiceResult> statisticalCompetitons() {
      return  ResponseEntity.ok(new ServiceResult( competitionService.statisticalsMyCompetition(),"success","200"));
  }


  @GetMapping("/competition/competition-category/{id}")
  public ResponseEntity<ServiceResult> getCategory(@PathVariable("id") Long idCate){
    return ResponseEntity.ok(new ServiceResult(cate.findByIds(idCate),"Thanh công","200"));
  }

}
