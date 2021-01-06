package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.CompetitionCategoryConverter;
import com.vnpost.elearning.converter.CompetitionConverter;
import com.vnpost.elearning.dto.competition.AnswerDTO;
import com.vnpost.elearning.dto.competition.CompetionCategoryDTO;
import com.vnpost.elearning.dto.competition.CompetitionDTO;
import com.vnpost.elearning.dto.competition.QuestionDTO;
import com.vnpost.elearning.dto.customDTO.ContentExamDTO;
import com.vnpost.elearning.dto.customDTO.QuestionRTCustomDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.TimeStartExam;
import eln.common.core.entities.competition.*;
import eln.common.core.repository.CandidateRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompetitionProcessor {
  private final QCompetition qCompetition = QCompetition.competition;
  private final QCompetitionCategory qCompetitionCategory =
      QCompetitionCategory.competitionCategory;
  private CandidateService candidateService;
  private CandidateRepository candidateRepository;
  private CompetitionService competitionService;
  private CompetitionConverter converter;
  private CompetitionCategoryService competitionCategoryService;
  private CompetitionCategoryConverter competitionCategoryConverter;
  private RoundTestService roundTestService;
  private PoscodeCompetitionService poscodeCompetitionService;
  private TimeStartExamService timeStartExamService;

  private final QRoundTest qRoundTest = QRoundTest.roundTest;

  public List<CompetitionDTO> findAll(CompetitionDTO competitionDTO, Pageable pageable)
      throws Exception {
    BooleanBuilder builder = commonBuilder(competitionDTO);
    return competitionService.findAll(builder, pageable).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public Long countAll(CompetitionDTO competitionDTO) throws Exception {
    BooleanBuilder builder = commonBuilder(competitionDTO);
    return competitionService.count(builder);
  }

  public List<CompetionCategoryDTO> findAllCategory(Long parentId) {
    BooleanBuilder builder = new BooleanBuilder();
    if (parentId != null) {
      builder.and(qCompetitionCategory.parent.eq(parentId));
    } else {
      builder.and(qCompetitionCategory.parent.isNull());
    }
    builder.and(qCompetitionCategory.status.eq(0));
    return competitionCategoryService.findAll(builder, PageRequest.of(0, 99999)).stream()
        .map(competitionCategoryConverter::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<RoundTest> findAllRoundTestCandidateByTypeMyCompetition(String type)
      throws Exception {
    BooleanBuilder builder = new BooleanBuilder();
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Candidate> candidates = new ArrayList<>();
    switch (type) {
      case SystemConstant.MY_COMPETITION_NOT_DONE:
        candidates = candidateRepository.findAllByUserIdAndConfirmAndPointNull(myUser.getId(), 0);
        if (candidates.size() <= 0) {
          builder.and(qRoundTest.id.eq(new Long(null)));
          break;
        }
        for (Candidate candidate :candidates) {
          builder.or(qRoundTest.candidates.contains(candidate));
        }
        break;
      case SystemConstant.MY_COMPETITION_DONE:
        candidates = candidateRepository.findAllByUserIdAndPointNotNull(myUser.getId());
        if (candidates.size() <= 0) {
          builder.and(qRoundTest.id.eq(new Long(null)));
          break;
        }
        for (Candidate candidate :candidates) {
          builder.or(qRoundTest.candidates.contains(candidate));
        }
        break;
      case SystemConstant.MY_COMPETITION_NOT_JOIN:
        candidates = candidateRepository.findAllByUserIdAndConfirm(myUser.getId(), 1);
        if (candidates.size() <= 0) {
          builder.and(qRoundTest.id.eq(new Long(null)));
          break;
        }
        for (Candidate candidate :candidates) {
          builder.or(qRoundTest.candidates.contains(candidate));
        }
        break;
      case SystemConstant.MY_COMPETITION_ALL:
        candidates = candidateRepository.findAllByUserId(myUser.getId());
        if (candidates.size() <= 0) {
          builder.and(qRoundTest.id.eq(new Long(null)));
          break;
        }
        for (Candidate candidate : candidates) {
          builder.or(qRoundTest.candidates.contains(candidate));
        }
        break;
      default:
        throw new Exception("Loại không hợp lệ");
    }
    List<RoundTest> result = new ArrayList<>();
    roundTestService.findAll(builder).forEach(result::add);
    return result;
  }

  private BooleanBuilder commonBuilder(CompetitionDTO competitionDTO) throws Exception {
    BooleanBuilder result = new BooleanBuilder();
    if (competitionDTO.getTypeMyCompetition() == null) {
      result.and(qCompetition.statusCompetition.eq(0));
    }

    result.and(qCompetition.checkcourseware.eq(0));
    if (competitionDTO == null) return result;
    if (StringUtils.isNotBlank(competitionDTO.getSearchValue()))
      result.and(qCompetition.nameCompetition.containsIgnoreCase(competitionDTO.getSearchValue()));
    if (StringUtils.isNotBlank(competitionDTO.getId_unit())) {
      result.and(qCompetition.poscodeVnpost.id.eq(competitionDTO.getId_unit()));
    }
    if (competitionDTO.getCategoryId() != null) {
      result.and(qCompetition.competitionCategory.id.eq(competitionDTO.getCategoryId()));
    }
    if (competitionDTO.getTypeMyCompetition() != null) {
      List<Long> idCompetitions = new ArrayList<>();
      for (RoundTest roundTest :
          findAllRoundTestCandidateByTypeMyCompetition(competitionDTO.getTypeMyCompetition())) {
        idCompetitions.add(roundTest.getCompetition().getId());
      }
      result.and(qCompetition.id.in(idCompetitions));
    }

    return result;
  }

  public Object[] getUnitConnect(Long idCompetition){
    try {
      String posCodeCreate = "";
      List<String> names = new ArrayList<>();
      List<String> namePoscodes = poscodeCompetitionService.findNameByIdCompetition(idCompetition);
      if (namePoscodes.size() > 0) {
        names.addAll(namePoscodes);
      }
      CompetitionDTO competitionDTO = competitionService.findByIds(idCompetition);
      if (competitionDTO.getPoscodeVnpost() != null) {
        posCodeCreate = competitionDTO.getPoscodeVnpost().getName();
      } else {
        posCodeCreate = "Tổng công ty Bưu Điện Việt Nam VIETNAMPOST";
      }
      String nameCompetition = competitionDTO.getNameCompetition();
      Object[] objects = {names, posCodeCreate, nameCompetition};
      return  objects;
    } catch (Exception e) {
          return  null;
    }
  }

  public ContentExamDTO contentTesst( Long id,RoundTest roundTest, MyUser user,   Integer countTest,Date date){
    try {
      List<QuestionRTCustomDTO> questions = new ArrayList<QuestionRTCustomDTO>();
      List<QuestionDTO> questionDTOS= roundTestService.getQuestionsByIdRoundTest(id);
      Date TimeStart =  saveTimeStart(id,user.getId(),countTest,date);
      int i = 0;
      HashSet<Integer> setListIndex = new HashSet<>();

      for (int index = 0; index < questionDTOS.size(); index++) {

        HashSet<Integer> setListIndexAnswer = new HashSet<>();
        Integer indexs = setIndexs(setListIndex, index, roundTest, questionDTOS.size());
        List<AnswerDTO> answersQuestion = new ArrayList<AnswerDTO>();
        i++;
        Integer sizeAnswers = questionDTOS.get(indexs).getAnswers().size();
        for (int indexAnser =0; indexAnser< sizeAnswers;indexAnser++) {
          Integer indexAnsers = setIndexAnswers(setListIndexAnswer, indexAnser, roundTest,sizeAnswers);
          answersQuestion.add( questionDTOS.get(indexs).getAnswers().get(indexAnsers));
        }
        questions.add(
                new QuestionRTCustomDTO(i,questionDTOS.get(indexs),
                        answersQuestion,questionDTOS.get(indexs).getTypeQuestion().getId()));
      }
      return new ContentExamDTO(questions,TimeStart,user.getId());
    }catch (Exception e){
      return null;
    }

  }

  private Integer setIndexAnswers(HashSet<Integer> setListIndexAnswer, int index, RoundTest roundTest, Integer size) {
    Random rand = new Random();
    if ((roundTest.getMixAnswer() + "").equals("0")) {
      Integer check = rand.nextInt(size);
      if (setListIndexAnswer.add(check)) {
        return check;
      } else {
        return setIndexAnswers(setListIndexAnswer, index, roundTest, size);
      }
    } else {
      return index;
    }
  }

  private Date saveTimeStart(Long idRound, Long idUser, Integer countTest,Date now) {
    Date timeStartInDB = timeStartExamService.findByidRoundidUserCount(idRound,idUser,countTest);
    if(timeStartInDB==null){
      TimeStartExam timeStartExam = new TimeStartExam(countTest,now,idRound,idUser);
      timeStartExamService.saveTime(timeStartExam);
      return now ;
    }else{
      return timeStartInDB;
    }
  }


  private Integer setIndexs(
          HashSet<Integer> setListIndex, int index, RoundTest roundTest, Integer size) {
    Random rand = new Random();
    if (!(roundTest.getMixCompetition().getId() + "").equals("1")) {
      Integer check = rand.nextInt(size);
      if (setListIndex.add(check)) {
        return check;
      } else {
        return setIndexs(setListIndex, index, roundTest, size);
      }
    } else {
      return index;
    }
  }



}
