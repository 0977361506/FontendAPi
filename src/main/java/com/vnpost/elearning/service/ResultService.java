package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.AnswerConverter;
import com.vnpost.elearning.converter.QuestionConverter;
import com.vnpost.elearning.dto.competition.AnswerDTO;
import com.vnpost.elearning.dto.competition.QuestionDTO;
import com.vnpost.elearning.dto.customDTO.CustomQRoundTestDTO;
import eln.common.core.common.StringGenerate;
import eln.common.core.entities.competition.*;
import eln.common.core.entities.question.Answer;
import eln.common.core.entities.question.Question;
import eln.common.core.repository.*;

import eln.common.core.repository.question.QuestionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultService {
  @Autowired private ResultRepository resultRepository;
  @Autowired private ResultRepository repository;
  @Autowired private AnswerRepository answerRepository;
  @Autowired private CandidateRepository candidateRepository;
  @Autowired private RoundTestRepository roundTestRepository;
  @Autowired private QuestionRoundTestService questionRoundTestService;
  @Autowired
  private AddPointRepository addPointRepository;
  @Autowired
  private SubPointRepository subPointRepository;
  @Autowired
  private AnswerConverter answerConverter;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private QuestionConverter questionConverter;
  private @Autowired CourseJoinService courseJoinService;

  public void saveResultSQL(
          Long idUser, Long idRound,
          List<CustomQRoundTestDTO> customQRoundTestDTOs, Integer counttest
          ,Long idCourse) {


    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String date = dateFormat.format(java.util.Calendar.getInstance().getTime());



    if (customQRoundTestDTOs.size() > 0) {
      resultRepository.deleteByErrorByIdUserIdRoundCount(idUser,idRound,counttest);
      answerRepository.updateForAnswerCodeNull();
      Integer point =  0;
      Integer result =  0;
      Integer sumpoint =  0;
      for (int i = 0; i < customQRoundTestDTOs.size(); i++) {
        QuestionDTO questionDTO = questionConverter.convertToDTO(questionRepository.findById(Long.parseLong(customQRoundTestDTOs.get(i).getIdQuestion())).get());
        List<Answer> answerList = answerRepository.findByIdQuestion(customQRoundTestDTOs.get(i).getIdQuestion());
        HashMap<String, String> addpoint = setMapAddPoint(idRound);
        HashMap<String, String> subpoint = setMapSubPoint(idRound);


        if (!customQRoundTestDTOs.get(i).getTypeQuestion().equals("4")) {
          if (customQRoundTestDTOs.get(i).getAnswerChecked().size() > 0) {

            for (String ansewrChecked : customQRoundTestDTOs.get(i).getAnswerChecked()) {

              repository.saveResult(
                      ansewrChecked, date, date, customQRoundTestDTOs.get(i).getIdQuestion(), idRound, idUser, "0", counttest);
            }
          }
          if (customQRoundTestDTOs.get(i).getAnswerNotChecked().size() > 0) {
            for (String ansewrNotChecked : customQRoundTestDTOs.get(i).getAnswerNotChecked()) {

              repository.saveResult(
                      ansewrNotChecked, date, date, customQRoundTestDTOs.get(i).getIdQuestion(), idRound, idUser, "1", counttest);
            }
          }
          Integer pointQuestionNotKind4 =  getPointQuestionNotKind4(customQRoundTestDTOs.get(i),addpoint,subpoint,questionDTO,answerList);
          result+=pointQuestionNotKind4>0?1:0;
          point+=pointQuestionNotKind4;


        } else {
          if (customQRoundTestDTOs.get(i).getAnswerNotChecked().size() > 0) {
            for (String ansewrNotChecked : customQRoundTestDTOs.get(i).getAnswerNotChecked()) {
              if (ansewrNotChecked.length() > 1) {
                repository.saveResult(
                        ansewrNotChecked.substring(1, 2), date, date, customQRoundTestDTOs.get(i).getIdQuestion(), idRound, idUser, ansewrNotChecked.substring(0, 1), counttest);
              } else if(ansewrNotChecked.length() >0 ){
                repository.saveResultTypeQuestion4Null(
                        ansewrNotChecked.substring(0, 1), date, date, customQRoundTestDTOs.get(i).getIdQuestion(), idRound, idUser, counttest);
              }
            }
          }
          List<Answer> answers = answerRepository.findByIdQuestion(customQRoundTestDTOs.get(i).getIdQuestion());
          for (Answer answer : answers) {
            if (isNumeric(answer.getAnswer())) {
              repository.saveResultTypeQuestion4Null(
                      answer.getAnswer(), date, date, customQRoundTestDTOs.get(i).getIdQuestion(), idRound, idUser, counttest);
            }
          }

          Integer pointQuestionKind4 =  getPointQuestionKind4(customQRoundTestDTOs.get(i),addpoint,subpoint,questionDTO,answerList);
          result+=pointQuestionKind4>0?1:0;
          point+=pointQuestionKind4;
        }
        sumpoint+=sumAddPoint(addpoint, questionDTO);
      }
      mathPointUser(idUser, idRound,counttest, idCourse,result,point,sumpoint,customQRoundTestDTOs.size());
    }

  }





  private Integer getPointQuestion(
          Integer count,
          Integer kt,
          QuestionDTO qs,
          HashMap<String, String> subpoint,
          HashMap<String, String> addpoint,
          Integer checked) {
    if (count == kt && kt == checked && count != 0) {
      return Integer.parseInt(addpoint.get(qs.getLevell().getId().toString()));
    } else {
      return -Integer.parseInt(subpoint.get(qs.getLevell().getId().toString()));
    }
  }
  private Integer sumAddPoint(HashMap<String, String> addpoint, QuestionDTO qs) {
    return Integer.parseInt(addpoint.get(qs.getLevell().getId().toString()));
  }

  private void mathPointUser(Long idUser, Long idRound,
                             Integer counttest,Long idCourse, Integer result,Integer point,Integer sumpoint
          ,Integer totalQuestion) {
    Candidate candidate =
            candidateRepository.findByIdUserIdRoundTestCountTest(
                    idUser.toString(), idRound.toString(), counttest.toString());
    RoundTest roundTest = roundTestRepository.findById(idRound).get();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String timeNow = dateFormat.format(new Date());
    float ratio_default = (float) roundTest.getMinPoint() / roundTest.getMaxPoint();
    float ratio_count = (float) point / sumpoint;
    Integer status = 1;
    if (ratio_default <= ratio_count) {
      status = 0;
      if(idCourse!=null){
        courseJoinService.updateFinish(idUser,idCourse);
      }
    }
    candidateRepository.updateCandidate(result,sumpoint,timeNow,timeNow,point,status,0,totalQuestion,candidate.getId());
  }

  private Integer getPointQuestionNotKind4(
          CustomQRoundTestDTO qRoundTestDTO, HashMap<String, String> addpoint
          ,HashMap<String, String> subpoint
          ,QuestionDTO questionDTO,List<Answer> answerList){
    Integer kt = 0;
    Integer count = 0;
    Integer checked = 0;
    checked = qRoundTestDTO.getAnswerChecked().size();
    if (qRoundTestDTO.getAnswerChecked().size() > 0) {
      for (int j = 0; j < answerList.size(); j++) {
        if (answerList.get(j).getAnswerCode() == 0) {
          count++;
        }
        for (String ansewrChecked : qRoundTestDTO.getAnswerChecked()) {
          if (answerList.get(j).getAnswerCode() == 0
                  && answerList.get(j).getAnswer().equals(ansewrChecked) ) {
            kt++;
          }
        }
      }
    }
    return getPointQuestion(count, kt, questionDTO, subpoint, addpoint, checked);
  }

  private Integer getPointQuestionKind4(
          CustomQRoundTestDTO qRoundTestDTO,
          HashMap<String, String> addpoint
          ,HashMap<String, String> subpoint,QuestionDTO questionDTO, List<Answer> answerList){
    Integer kt = 0;
    Integer count = 0;
    Integer checked = 0;
    checked = qRoundTestDTO.getAnswerChecked().size();
    if (qRoundTestDTO.getAnswerNotChecked().size() > 0) {
      checked = qRoundTestDTO.getAnswerNotChecked().size();
      for (int j = 0; j < answerList.size(); j++) {
        if (answerList.get(j).getAnswerCode() != null) {
          kt++;
        }
        for (String ansewrNotChecked : qRoundTestDTO.getAnswerNotChecked()) {
          if (ansewrNotChecked.length() > 1) {
            if (!StringGenerate.isNumeric(answerList.get(j).getAnswer())) {
              if (answerList.get(j).getAnswerCode()
                      == Integer.parseInt(ansewrNotChecked.substring(0, 1))
                      && answerList.get(j).getAnswer().equals(ansewrNotChecked.substring(1, 2))) {
                count++;
              }
            }
          }
        }
      }
    }
    return getPointQuestion(count, kt, questionDTO, subpoint, addpoint, checked);
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  public List<QuestionDTO> getListQuestionRoundTest(Long id_user, String countTime, Long id_round_test) {
    Map<String, Object> map = setMapQuestionRoundTest(id_user, countTime, id_round_test);
    List<QuestionDTO> questionList = ((List<Question>) questionRepository
            .getQuestionByIdRoundIdUserCountTest(id_round_test,id_user,Integer.parseInt(countTime))).stream()
            .map(questionConverter::convertToDTO).collect(Collectors.toList());
    setValuesQuestionRoundTest(questionList, countTime, id_round_test, id_user);
    return questionList;

  }
  private Map<String, Object> setMapQuestionRoundTest(Long id_user, String countTime, Long id_round_test) {
    Map<String, Object> map = new HashMap<>();
    map.put("id_user",id_user);
    map.put("counttest",countTime);
    map.put("id_round_test", id_round_test);
    return map;

  }
  private void setValuesQuestionRoundTest(List<QuestionDTO> questionDTOS, String countTime, Long id_round_test,Long id_user) {
    HashMap<String, String> addpoint = setMapAddPoint(id_round_test);
    HashMap<String, String> subpoint = setMapSubPoint(id_round_test);
    RoundTest roundTest = roundTestRepository.findById(id_round_test).get();
    for (QuestionDTO qs : questionDTOS) {
      List<Answer> answerList = answerRepository.findByIdQuestion(qs.getId() + "");
      List<Result> resultList = resultRepository.findByIdQuestionnativeQuery(qs.getId(), countTime, id_round_test,id_user);
      List<AnswerDTO> answerDTOList = setAnswerDTO(answerList);
      qs.setListAnswerDTOS(answerDTOList);
      Integer kt = 0;
      Integer count = 0;
      Integer countResult = 0;
      if (qs.getTypeQuestion().getId() != 4) {
        if (resultList.size() > 0) {
          for (int i = 0; i < answerDTOList.size(); i++) {
            if (answerDTOList.get(i).getAnswerCode() == 0) {
              count++;
            }
            if (answerDTOList.get(i).getAnswerCode() == resultList.get(i).getAnswerCode() && resultList.get(i).getAnswerCode() == 0) {
              kt++;
            }
            if (resultList.get(i).getAnswerCode() == 0) {
              countResult++;
              answerDTOList.get(i).setYouChose("0");
            }
            checkViewResultRoundTest(qs,countTime,roundTest,i);
          }
        }
        setqs(count, kt, qs, subpoint, addpoint, countResult);
      } else {
        if (resultList.size() > 0) {
          for (int i = 0; i < answerList.size(); i++) {
            if (!StringGenerate.isNumeric(answerList.get(i).getAnswer())) {
              if (answerList.get(i).getAnswerCode() == resultList.get(i).getAnswerCode()) {
                count++;
              }
              if (answerList.get(i).getAnswerCode() != null) {
                kt++;
                countResult++;
              }
              answerDTOList.get(i).setYouChose(resultList.get(i).getAnswerCode() + "");
            }

            checkViewResultRoundTest(qs,countTime,roundTest,i);
          }
        }
        setqs(count, kt, qs, subpoint, addpoint, countResult);
      }



    }
  }

  private void checkViewResultRoundTest(QuestionDTO questionDTO, String countTime, RoundTest roundTest, Integer i) {
    //if(roundTest.getCompetition().getCheckcourseware()==0){
    if(roundTest.getDoAgain()==0 && roundTest.getMaxWork()==0) {
      setShowResult(questionDTO,i);
    } else if(roundTest.getMaxWork()==0) {
      setShowResult(questionDTO,i);
    }else if (!(countTime).equals(roundTest.getMaxWork().toString())) {
      setShowResult(questionDTO,i);
    }else if ((countTime).equals(roundTest.getMaxWork().toString())) {
      if(roundTest.getShowAnswer()!=0){
        questionDTO.setResultDTOList(null);
        questionDTO.getListAnswerDTOS().get(i).setAnswerCode(null);
      }
      if(roundTest.getShowExplain()!=0) {
        questionDTO.setExplain(null);
      }
    }
    // }
  }

  private void setShowResult(QuestionDTO questionDTO,Integer i) {
    questionDTO.setResultDTOList(null);
    questionDTO.getListAnswerDTOS().get(i).setAnswerCode(null);
    questionDTO.setExplain(null);
  }

  private List<AnswerDTO> setAnswerDTO(List<Answer> answerList) {
    List<AnswerDTO> answerDTOList = new ArrayList<>();
    for (Answer answer : answerList) {
      AnswerDTO answerDTO = answerConverter.convertToDTO(answer);
      answerDTOList.add(answerDTO);
    }
    return answerDTOList;
  }
  private void setqs(
          Integer count,
          Integer kt,
          QuestionDTO qs,
          HashMap<String, String> subpoint,
          HashMap<String, String> addpoint,
          Integer countResult) {
    if (count == kt && kt == countResult && count != 0) {
      qs.setIsCorrect(0);
      qs.setAddOrSub(addpoint.get(qs.getLevell().getId() + ""));
    } else {
      qs.setIsCorrect(1);
      qs.setAddOrSub("- " + subpoint.get(qs.getLevell().getId() + ""));
    }
  }

  private HashMap<String, String> setMapAddPoint(Long id_round_test) {
    HashMap<String, String> map = new HashMap<String, String>();
    List<AddPoint> list = addPointRepository.findByIdRoundTest(id_round_test.toString());
    for (AddPoint a : list) map.put(a.getLevells().getId() + "", a.getAddPoint() + "");
    return map;
  }

  private HashMap<String, String> setMapSubPoint(Long id_round_test) {
    HashMap<String, String> map = new HashMap<String, String>();
    List<SubPoint> list = subPointRepository.findByIdRoundTest(id_round_test + "");
    for (SubPoint a : list) {
      map.put(a.getLevells().getId() + "", a.getSub() + "");
    }
    return map;
  }


}
