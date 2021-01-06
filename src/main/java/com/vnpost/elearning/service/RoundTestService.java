package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.QuestionConverter;
import com.vnpost.elearning.converter.RoundTestConverter;
import com.vnpost.elearning.dto.competition.QuestionDTO;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.repository.QuestionCustomRepository;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.competition.Candidate;
import eln.common.core.entities.competition.RoundTest;
import eln.common.core.entities.competition.StructDetailTest;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.question.Question;
import eln.common.core.entities.question.QuestionRoundTest;
import eln.common.core.repository.*;
import eln.common.core.repository.question.QuestionRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RoundTestService extends CommonRepository<RoundTest,RoundTestRepository> {
    @Autowired
    private RoundTestRepository roundTestRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RoundTestConverter roundTestConverter;
    @Autowired
    private QuestionCustomRepository questionCustomRepository;
    @Autowired
    private QuestionRoundTestRepository questionRoundTestRepository;
    @Autowired
    private StructDetailTestRepository structDetailTestRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionConverter questionConverter;

    @Autowired
    EntityManager entityManager;

    public RoundTestService(RoundTestRepository repo) {
        super(repo);
    }

    public void save(RoundTestDTO roundTestDTO) {
        RoundTest roundTest = roundTestConverter.convertToEntity(roundTestDTO);
        roundTestRepository.save(roundTest);

    }

    public List<RoundTestDTO> findByIdCompetition(long id) {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return setCheckFinishRoundTest(roundTestRepository
                .findAllByCompetitionId(id).stream().map(roundTestConverter::convertToDTO)
                .collect(Collectors.toList()),myUsers.getId());
    }

    private String setStatusCandidate(List<Candidate> candidate) {
        try {
            if(candidate.size()>1){
                return SystemConstant.MY_COMPETITION_DONE;
            }else if(candidate.size()==1){
                if(candidate.get(0).getConfirm()==1){
                    return SystemConstant.MY_COMPETITION_WAIT_CONFIRM;
                }else{
                    if(candidate.get(0).getPoint()==null){
                        return SystemConstant.MY_COMPETITION_NOT_DONE;
                    }else{
                        return SystemConstant.MY_COMPETITION_DONE;
                    }
                }
            }else{
                return SystemConstant.MY_COMPETITION_NOT_JOIN;
            }
        }catch (Exception e){
            return null;
        }
    }



    public RoundTestDTO update(RoundTestDTO roundTestDTO) {
        RoundTest roundTest = roundTestConverter.convertToEntity(roundTestDTO);
        roundTest = roundTestRepository.save(roundTest);
        roundTestDTO = roundTestConverter.convertToDTO(roundTest);
        return roundTestDTO;
    }


    public RoundTestDTO findById(long id) {
        try {
            RoundTestDTO roundTestDTO = roundTestConverter.convertToDTO(roundTestRepository.findById(id).get());
            return roundTestDTO;
        } catch (Exception e) {
            return null;
        }

    }


    public void delete(RoundTestDTO roundTestDTO) {
        roundTestRepository.delete(roundTestConverter.convertToEntity(roundTestDTO));
    }


    public RoundTest findByid(Long id) {
        return entityManager.unwrap(Session.class).find(RoundTest.class, id);
    }


    public RoundTest save(RoundTest entity) {
        return roundTestRepository.save(entity);
    }


    public List<Object[]> getListQuestionForRountest(Long id) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("getListQuestionForRoundTesst")
                .registerStoredProcedureParameter(
                        "id", Long.class, ParameterMode.IN)
//
                .setParameter("id", id);

        query.execute();
        List<Object[]> result = query.getResultList();

        return result;
    } //getListAnswerForRoundTesst

    public List<Object[]> getListAnswerForRoundTesst() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("getListAnswerForRoundTesst");

        query.execute();
        List<Object[]> result = query.getResultList();

        return result;
    }

    public List<QuestionDTO> getQuestionsByIdRoundTest(Long id) {
        RoundTestDTO roundTestDTO = roundTestConverter.convertToDTO(roundTestRepository.findById(id).get());
        if(roundTestDTO.getAutoCreateQuestion()==0){
            List<QuestionDTO> questionRoundTestDTOList = rumdomQuestionRound(roundTestDTO);
            return questionRoundTestDTOList;
        }else{
            List<QuestionDTO> questionRoundTestDTOList = questionRepository.getQuestionsByIdRoundTest(id)
                    .stream().map(item -> questionConverter.convertToDTO(item)).collect(Collectors.toList());
            return questionRoundTestDTOList;
        }
    }

    private List<QuestionDTO> rumdomQuestionRound( RoundTestDTO roundTestDTO) {
        List<QuestionDTO> questionDTOArrayList = new ArrayList<>();
        HashSet<Long> idQuestion = new HashSet<>();
        List<StructDetailTest> structDetailTestDTOList = structDetailTestRepository.findByIdStructTest(roundTestDTO.getStructTest().getId().toString());
        for (StructDetailTest structDetailTest : structDetailTestDTOList) {
            Map<String, Object> map = new HashMap<>();
            if (structDetailTest.getLevell() != null) {
                map.put("id_level", structDetailTest.getLevell().getId());
            }
            if (structDetailTest.getTag() != null) {
                map.put("id_tag", structDetailTest.getTag().getId());
            }
            if (structDetailTest.getTypeQuestion() != null) {
                map.put("id_type_question", structDetailTest.getTypeQuestion().getId());
            }
            if (structDetailTest.getQuestionCategoryStruct() != null) {
                map.put("id_question_category", structDetailTest.getQuestionCategoryStruct().getId());
            }
            map.put("status_question", 0);

            Object[] objects = questionCustomRepository.findByPropertyRandom(map,structDetailTest.getCountTest());

            try {
            List<Question>  questionList = (List<Question>) objects[0];
            if(questionList.size()>0){
                for (int i = 0; i < questionList.size(); i++) {
                            questionDTOArrayList.add(questionConverter
                                    .convertToDTO(questionList.get(i)));
                }
            }
            }catch (Exception e){ }
        }

        return questionDTOArrayList;
    }




    public List<QuestionRoundTest> getListQuestionRountest(Long id) {
        String hqlString = "from QuestionRoundTest q where q.roundTest.id=:cid ";
        List<QuestionRoundTest> list = entityManager.unwrap(Session.class).createQuery(hqlString).setParameter("cid", id).getResultList();
        return list;
    }




    public List<RoundTestDTO> findAllRoundTestByCompetitionId(Long competitionId) throws Exception {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return setCheckFinishRoundTest(roundTestRepository
                .findAllByCompetitionId(competitionId).stream().map(roundTestConverter::convertToDTO)
                .collect(Collectors.toList()),myUsers.getId());
    }

    private List<RoundTestDTO> setCheckFinishRoundTest(List<RoundTestDTO> collect,Long id) {
        for (RoundTestDTO roundTestDTO:collect){
            roundTestDTO.setMessage(setStatusCandidate(candidateRepository.findByUserIdAndRoundTestId(id,roundTestDTO.getId())));
        }
        return collect;
    }

    public Long getListIdRound(Course course) {
        if (course.getCompetitionId() != null) {
            List<RoundTest> roundTestDTOList = roundTestRepository.findByIdCompetition(course.getCompetitionId());
            if (roundTestDTOList.size() > 0) {
                return roundTestDTOList.get(0).getId();
            }
        }
        return null;
    }



}
