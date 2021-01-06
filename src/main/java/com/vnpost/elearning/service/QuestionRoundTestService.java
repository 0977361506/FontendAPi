package com.vnpost.elearning.service;


import org.springframework.stereotype.Service;


@Service
public class QuestionRoundTestService {

//  @Autowired private QuestionRoundTestRepository questionRoundTestRepository;
//  @Autowired private QuestionRoundTestConverter questionRoundTestConverter;
//  @Autowired private AddPointRepository addPointRepository;
//  @Autowired private SubPointRepository subPointRepository;
//  @Autowired private AnswerRepository answerRepository;
//  @Autowired private ResultRepository resultRepository;
//  @Autowired private AnswerConverter answerConverter;
//  @Autowired private RoundTestRepository roundTestRepository;
//  @Autowired private QuestionRepository questionRepository;
//  @Autowired private QuestionConverter questionConverter;
//  @Autowired private QuestionRoundTestCustom questionRoundTestCustom;

//  private Integer getPointQuestion(
//      Integer count,
//      Integer kt,
//      QuestionDTO qs,
//      HashMap<String, String> subpoint,
//      HashMap<String, String> addpoint,
//      Integer countResult) {
//    if (count == kt && kt == countResult && count != 0) {
//
//      return Integer.parseInt(addpoint.get(qs.getLevell().getId() + ""));
//
//    } else {
//      return -Integer.parseInt(subpoint.get(qs.getLevell().getId() + ""));
//    }
//  }

//  private void setqs(
//      Integer count,
//      Integer kt,
//      QuestionRoundTestDTO qs,
//      HashMap<String, String> subpoint,
//      HashMap<String, String> addpoint,
//      Integer countResult) {
//    if (count == kt && kt == countResult && count != 0) {
//      qs.setIsCorrect(0);
//      qs.setAddOrSub(addpoint.get(qs.getQuestion().getLevell().getId() + ""));
//    } else {
//      qs.setIsCorrect(1);
//      qs.setAddOrSub("- " + subpoint.get(qs.getQuestion().getLevell().getId() + ""));
//    }
//  }

//  private HashMap<String, String> setMapAddPoint(Long id_round_test) {
//    HashMap<String, String> map = new HashMap<String, String>();
//    List<AddPoint> list = addPointRepository.findByIdRoundTest(id_round_test + "");
//    for (AddPoint a : list) map.put(a.getLevells().getId() + "", a.getAddPoint() + "");
//    return map;
//  }
//
//  private HashMap<String, String> setMapSubPoint(Long id_round_test) {
//    HashMap<String, String> map = new HashMap<String, String>();
//    List<SubPoint> list = subPointRepository.findByIdRoundTest(id_round_test + "");
//    for (SubPoint a : list) {
//      map.put(a.getLevells().getId() + "", a.getSub() + "");
//    }
//    return map;
//  }
//
//  private Map<String, Object> setMapQuestionRoundTest(
//      Long id_user, String countTime, Long id_round_test) {
//    Map<String, Object> map = new HashMap<>();
//    /*  map.put("id_user",id_user);*/
//    //         map.put("counttest",countTime);
//    map.put("id_round_test", id_round_test);
//
//    return map;
//  }

//  public Object[] setPointRoundTesst(Long idu, String countTime, Long id_round_test) {
//    Map<String, Object> map = setMapQuestionRoundTest(idu, countTime, id_round_test);
//    List<QuestionDTO> questionDTOS =
//            ((List<Question>) questionRepository.getQuestionByIdRoundIdUserCountTest(id_round_test,idu,Integer.parseInt(countTime)))
//            .stream().map(questionConverter::convertToDTO).collect(Collectors.toList());
//    Integer diem = 0;
//    Integer so = 0;
//    HashMap<String, String> addpoint = setMapAddPoint(id_round_test);
//    HashMap<String, String> subpoint = setMapSubPoint(id_round_test);
//
//
//
//    for (QuestionDTO qs : questionDTOS) {
//      List<Answer> answerList = answerRepository.findByIdQuestion(qs.getId() + "");
//      List<Result> resultList =
//              resultRepository.findByIdQuestionnativeQuery(qs.getId(), countTime, id_round_test,idu);
//
//      Integer kt = 0;
//      Integer count = 0;
//      Integer countResult = 0;
//      if (qs.getTypeQuestion().getId() != 4) {
//        if (resultList.size() > 0) {
//          for (int i = 0; i < answerList.size(); i++) {
//            if (answerList.get(i).getAnswerCode() == 0) {
//              count++;
//            }
//            if (answerList.get(i).getAnswerCode() == resultList.get(i).getAnswerCode()
//                    && resultList.get(i).getAnswerCode() == 0) {
//              kt++;
//            }
//            if (resultList.get(i).getAnswerCode() == 0) {
//              countResult++;
//            }
//          }
//        }
//        Integer diem_1 =
//                getPointQuestion(count, kt, qs, subpoint, addpoint, countResult);
//        if (diem_1 > 0) {
//          so++;
//        }
//        diem += diem_1;
//      } else {
//        if (resultList.size() > 0) {
//          for (int i = 0; i < answerList.size(); i++) {
//            if (!StringGenerate.isNumeric(answerList.get(i).getAnswer())) {
//              if (answerList.get(i).getAnswerCode() == resultList.get(i).getAnswerCode()) {
//                count++;
//              }
//              if (answerList.get(i).getAnswerCode() != null) {
//                kt++;
//                countResult++;
//              }
//            }
//          }
//        }
//        Integer diem_1 =
//                getPointQuestion(count, kt, qs, subpoint, addpoint, countResult);
//        if (diem_1 > 0) {
//          so++;
//        }
//        diem += diem_1;
//      }
//    }
//    return new Object[] {diem, so};
//  }


//  public List<QuestionRoundTestDTO> getListQuestionRoundTest(Long id_user, String countTime, Long id_round_test) {
//    Map<String, Object> map = setMapQuestionRoundTest(id_user, countTime, id_round_test);
//    List<QuestionRoundTest> questionRoundTestList = (List<QuestionRoundTest>) questionRoundTestCustom.findByPropertyAdminIdQuestionNull(map)[0];
//    List<QuestionRoundTestDTO> questionRoundTestDTOList = new ArrayList<>();
//    for (QuestionRoundTest q : questionRoundTestList) {
//      QuestionRoundTestDTO questionRoundTestDTO = questionRoundTestConverter.convertToDTO(q);
//      questionRoundTestDTOList.add(questionRoundTestDTO);
//    }
//    setValuesQuestionRoundTest(questionRoundTestDTOList, countTime, id_round_test,id_user);
//
//    return questionRoundTestDTOList;
//  }



//  private void setValuesQuestionRoundTest(List<QuestionRoundTestDTO> questionRoundTestDTOList, String countTime, Long id_round_test,Long id_user) {
//    HashMap<String, String> addpoint = setMapAddPoint(id_round_test);
//    HashMap<String, String> subpoint = setMapSubPoint(id_round_test);
//    RoundTest roundTest = roundTestRepository.findById(id_round_test).get();
//    for (QuestionRoundTestDTO qs : questionRoundTestDTOList) {
//      List<Answer> answerList = answerRepository.findByIdQuestion(qs.getQuestion().getId() + "");
//      List<Result> resultList = resultRepository.findByIdQuestionnativeQuery(qs.getQuestion().getId(), countTime, id_round_test,id_user);
//      List<AnswerDTO> answerDTOList = setAnswerDTO(answerList);
//      qs.setListAnswerDTOS(answerDTOList);
//      Integer kt = 0;
//      Integer count = 0;
//      Integer countResult = 0;
//      if (qs.getQuestion().getTypeQuestion().getId() != 4) {
//        if (resultList.size() > 0) {
//          for (int i = 0; i < answerDTOList.size(); i++) {
//            if (answerDTOList.get(i).getAnswerCode() == 0) {
//              count++;
//            }
//            if (answerDTOList.get(i).getAnswerCode() == resultList.get(i).getAnswerCode() && resultList.get(i).getAnswerCode() == 0) {
//              kt++;
//            }
//            if (resultList.get(i).getAnswerCode() == 0) {
//              countResult++;
//              answerDTOList.get(i).setYouChose("0");
//            }
//            checkViewResultRoundTest(qs,countTime,roundTest,i);
//          }
//        }
//        setqs(count, kt, qs, subpoint, addpoint, countResult);
//      } else {
//        if (resultList.size() > 0) {
//          for (int i = 0; i < answerList.size(); i++) {
//            if (!StringGenerate.isNumeric(answerList.get(i).getAnswer())) {
//              if (answerList.get(i).getAnswerCode() == resultList.get(i).getAnswerCode()) {
//                count++;
//              }
//              if (answerList.get(i).getAnswerCode() != null) {
//                kt++;
//                countResult++;
//              }
//              answerDTOList.get(i).setYouChose(resultList.get(i).getAnswerCode() + "");
//            }
//
//            checkViewResultRoundTest(qs,countTime,roundTest,i);
//          }
//        }
//        setqs(count, kt, qs, subpoint, addpoint, countResult);
//      }
//
//
//
//    }
//  }

//  private void checkViewResultRoundTest(QuestionRoundTestDTO questionRoundTestDTO, String countTime, RoundTest roundTest,Integer i) {
//    //if(roundTest.getCompetition().getCheckcourseware()==0){
//      if(roundTest.getDoAgain()==0 && roundTest.getMaxWork()==0) {
//        setShowResult(questionRoundTestDTO,i);
//      } else if(roundTest.getMaxWork()==0) {
//        setShowResult(questionRoundTestDTO,i);
//      }else if (!(countTime).equals(roundTest.getMaxWork().toString())) {
//        setShowResult(questionRoundTestDTO,i);
//      }else if ((countTime).equals(roundTest.getMaxWork().toString())) {
//        if(roundTest.getShowAnswer()!=0){
//          questionRoundTestDTO.setResultDTOList(null);
//          questionRoundTestDTO.getListAnswerDTOS().get(i).setAnswerCode(null);
//        }
//        if(roundTest.getShowExplain()!=0) {
//          questionRoundTestDTO.getQuestion().setExplain(null);
//        }
//      }
//   // }
//  }

//  private void setShowResult(QuestionRoundTestDTO questionRoundTestDTO,Integer i) {
//    questionRoundTestDTO.setResultDTOList(null);
//    questionRoundTestDTO.getListAnswerDTOS().get(i).setAnswerCode(null);
//    questionRoundTestDTO.getQuestion().setExplain(null);
//  }
//
//  private List<AnswerDTO> setAnswerDTO(List<Answer> answerList) {
//    List<AnswerDTO> answerDTOList = new ArrayList<>();
//    for (Answer answer : answerList) {
//      AnswerDTO answerDTO = answerConverter.convertToDTO(answer);
//      answerDTOList.add(answerDTO);
//    }
//    return answerDTOList;
//  }

}
