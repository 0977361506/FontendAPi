package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.CompetitionConverter;

import com.vnpost.elearning.dto.customDTO.CustomProcessCompeDTO;

import com.vnpost.elearning.dto.competition.CompetionCategoryDTO;
import com.vnpost.elearning.dto.competition.CompetitionDTO;
import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import com.vnpost.elearning.dto.competition.RoundTestDTO;

import com.vnpost.elearning.dto.customDTO.StatisticalMyCompetitionDTO;
import com.vnpost.elearning.repository.CompetitionCustom;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.competition.Candidate;
import eln.common.core.entities.competition.Competition;
import eln.common.core.entities.competition.CompetitionCategory;
import eln.common.core.repository.*;
import eln.common.core.repository.competition.CompetitionCategoryRepository;
import eln.common.core.repository.competition.CompetitionRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class CompetitionService extends CommonRepository<Competition, CompetitionRepository> {
    @Autowired private CompetitionRepository competitionRepository;
    @Autowired private RoundTestRepository roundTestRepository;
    @Autowired EntityManager entityManager;
    @Autowired private CompetitionConverter competitionConverter;
    @Autowired private CompetitionCategoryService categoryService;
    @Autowired private RoundTestService roundTestService;
    @Autowired private PoscodeVnpostService poscodeVnpostService;
    @Autowired EntityManagerFactory entityManagerFactory;
    @Autowired private CompetitionCustom competitionCustom;
    @Autowired private CompetitionCategoryRepository competitionCategoryRepository;
    @Autowired private CandidateRepository candidateRepository;


    public CompetitionService(CompetitionRepository repo) {
        super(repo);
    }



    public CompetitionDTO save(CompetitionDTO competitionDTO) {
        Competition competition = competitionConverter.convertToEntity(competitionDTO);
        competitionDTO = competitionConverter.convertToDTO(competitionRepository.save(competition));

        return competitionDTO;
    }




    public CompetitionDTO findByIds(long id) {
        CompetitionDTO competitionDTO =
                competitionConverter.convertToDTO(competitionRepository.findById(id).get());
        return competitionDTO;
    }

    public Object[] getListCompetitions(CompetitionDTO model) {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer offset = model.getFirstItem();
        try {
            if (model.getPage() != null && model.getPage() > 1) {
                offset = ((model.getPage() - 1) * model.getMaxPageItems());
            }
            setValueMapHomepage(model, map);
            Object[] objects =
                    competitionCustom.findByPropertyLikeSQL(
                            map, "c.time_create", "2", offset, model.getMaxPageItems());
            List<CompetitionDTO> list = new ArrayList<>();
            for (Competition competition : (List<Competition>) objects[0]) {
                CompetitionDTO competitionDTO = competitionConverter.convertToDTO(competition);
                list.add(competitionDTO);
            }
            objects[0] = list;
            return objects;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }





    private void setValueMapHomepage2(CompetitionDTO model, Map<String, Object> map) {
        if (model.getNameCompetition() != null) {
            map.put("co.name_competition", model.getNameCompetition());
        }
        if (model.getCategory_value() != null) {
            map.put("id_competition_category", model.getCategory_value());

        }
        map.put("co.checkcourseware", 0);
    }

    private void setListCategoryCompetition(Map<String, Object> map,String category_value) {

        List<CompetitionCategory> categoryLists = competitionCategoryRepository.findAll();
        HashSet<Long> longHashSet = new HashSet<>();
        List<Long> idCategoryNeed = new ArrayList<>();
        List<Long> idTemporarys = new ArrayList<>();
        idTemporarys.add(Long.parseLong(category_value));
        idCategoryNeed.add(Long.parseLong(category_value));
        getListIdParent(categoryLists,longHashSet,idCategoryNeed,idTemporarys);
        setStringMap(map,idCategoryNeed);
    }

    private void setStringMap(Map<String, Object> map, List<Long> idCategoryNeed) {
        StringBuilder  result = new StringBuilder();
        result.append("( ");
        for (int i=0;i<idCategoryNeed.size();i++){
            if(i<idCategoryNeed.size()-1){
                result.append(idCategoryNeed.get(i).toString()).append(" , ");
            }else{
                result.append(idCategoryNeed.get(i).toString());
            }
        }
        result.append(" )");
        map.put("idParents",result.toString());
    }

    private Integer getListIdParent(List<CompetitionCategory> categoryLists,
                HashSet<Long> longHashSet,List<Long> idCategoryNeed, List<Long> idTemporarys) {
        List<Long>  idParents = idTemporarys;
        List<Long> idTemporary = new ArrayList<>();
        if(idParents.size()<=0){
            return 0;
        }
        for (Long id:idParents){
         for (CompetitionCategory category :categoryLists){

             if(category.getParent()!=null){
                 if((category.getParent().toString()).equals(id.toString())){
                     if(longHashSet.add(category.getId())){
                         idCategoryNeed.add(category.getId());
                         idTemporary.add(category.getId());
                     }else{
                         return  0;
                     }
                 }
             }
           }
        }
        idTemporarys = idTemporary;
        return  getListIdParent(categoryLists,longHashSet,idCategoryNeed,idTemporarys);
    }

    private void setValueMapHomepage(CompetitionDTO model, Map<String, Object> map) {
        map.put("status_competition", 0);
        if (model.getNameCompetition() != null) {
            map.put("c.name_competition", model.getNameCompetition());
        }
        if (model.getCategory_value() != null) {
           // map.put("id_competition_category", model.getCategory_value());
            setListCategoryCompetition(map, model.getCategory_value());
        }
        map.put("c.checkcourseware", 0);
    }


    public CompetitionDTO getDetailCompetition(Long idCompetition){
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompetitionDTO competitionDTO = competitionConverter.convertToDTO(competitionRepository.findById(idCompetition).get());
        setcustomProcessCompeDTOs(competitionDTO,myUsers.getId());
        return competitionDTO;
    }



    public List<Competition> getHightLight() {
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        String hqlString =
                "from Competition d where d.statusCompetition=0 and d.checkcourseware=0 and d.highlight=0 order by d.timeCreate desc ";
        Query query = session.createQuery(hqlString).setFirstResult(0).setMaxResults(6);
        List<Competition> list = query.getResultList();
        return list;
    }

    public Competition findById_jonas(long id) {
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        return session.find(Competition.class, id);
    }






  public Object[] getListCompetitionMyCompe(CompetitionDTO model) {
      MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Map<String, Object> map = new HashMap<String, Object>();
      Integer offset = setOffset(model);
      try {
          setValueMapHomepage2(model, map);
          Object[] objects =
                  competitionCustom.findByPropertyLikeSQLMyCompe(
                          map, user.getId(), offset, model.getMaxPageItems(),model.getCheckFinish());
          List<CompetitionDTO> list =
                  ((List<Competition>) objects[0])
                          .stream()
                          .map(item -> competitionConverter.convertToDTO(item))
                          .collect(Collectors.toList());
          objects[0] = list;
          setStatusRoundTest(list, user.getId());
          return objects;

      } catch (Exception e) {
          System.out.println(e.getMessage());
          return null;
      }
  }

    public CompetitionDTO update(CompetitionDTO competitionDTO) {
        CompetitionDTO compet = setUpdateCompetition(competitionDTO);
        CompetitionDTO cDto =
                competitionConverter.convertToDTO(
                        competitionRepository.save(competitionConverter.convertToEntity(compet)));
        saveRoundTest(competitionDTO, cDto);
        return cDto;

    }


    private CompetionCategoryDTO getcompetitionCategory(String category_value) {
        CompetionCategoryDTO categoryDTO = categoryService.findByIds(Long.parseLong(category_value));
        return categoryDTO;
    }

    private CompetitionDTO setUpdateCompetition(CompetitionDTO competitionDTO) {
        Competition compe = competitionRepository.findById(competitionDTO.getId()).get();
        CompetitionDTO cDto = new CompetitionDTO();
        cDto.setId(competitionDTO.getId());
        cDto.setNameCompetition(competitionDTO.getNameCompetition());
        cDto.setStatusCompetition(Integer.parseInt(competitionDTO.getStatus_search_value()));
        cDto.setCompetitionCategory(getcompetitionCategory(competitionDTO.getCategory_value()));
        cDto.setDescribe(competitionDTO.getDescribe());
        cDto.setPoscodeVnpost(getPoscodeVnpsot(competitionDTO.getId_unit()));
        cDto.setTimeCreate(compe.getTimeCreate());
        cDto.setLastUpdate(java.util.Calendar.getInstance().getTime());
        cDto.setImageCompetition(competitionDTO.getImageCompetition());
        if (competitionDTO.getTimeEnd() != null && competitionDTO.getTimeStart() != null) {

            cDto.setTimeStart(competitionDTO.getTimeStart());
            cDto.setTimeEnd(competitionDTO.getTimeStart());
        }

        return cDto;
    }

    private PoscodeVnpostDTO getPoscodeVnpsot(String id_unit) {
        PoscodeVnpostDTO poscodeVnpostDTO = poscodeVnpostService.findById(id_unit);
        return poscodeVnpostDTO;
    }

    private void saveRoundTest(CompetitionDTO competitionDTO, CompetitionDTO campeSaved) {
        if (competitionDTO.getListArray() != null) {

            for (String s : competitionDTO.getListArray()) {
                RoundTestDTO roundTestDTO = new RoundTestDTO();
                roundTestDTO.setNameRound(s);
                roundTestDTO.setStatusRound(1);
                roundTestDTO.setCompetition(campeSaved);
                roundTestDTO.setTimeCreate(java.util.Calendar.getInstance().getTime());
                roundTestDTO.setLastUpdate(java.util.Calendar.getInstance().getTime());
                roundTestService.save(roundTestDTO);
            }

        }
    }


    public Competition findByCourseId(Long courseId) {
        return competitionRepository.findByCourseId(courseId);
    }

    public List<CompetitionDTO> findAllActiveCompetition() {
        return competitionRepository.findAllByStatusCompetitionAndCheckcourseware(0,0).stream()
                .map(competitionConverter::convertToDTO)
                .collect(Collectors.toList());
    }


    public List<CompetitionDTO> getListCompetitionHisCompe() {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return competitionRepository.getListCompetitionHisCompe(user.getId()).stream()
                .map(item -> competitionConverter.convertToDTO(item)).collect(Collectors.toList());
    }

    private Integer setOffset(CompetitionDTO model) {
        if (model.getPage() != null && model.getPage() > 1) {
            return ((model.getPage() - 1) * model.getMaxPageItems());
        }
        return model.getFirstItem();
    }




    private void setStatusRoundTest(List<CompetitionDTO> list,Long idUser) {
        try {
            for (CompetitionDTO competitionDTO : list) {
                setcustomProcessCompeDTOs(competitionDTO,idUser);
            }
        }catch (Exception e){}
    }

    private void setcustomProcessCompeDTOs(CompetitionDTO competitionDTO,Long idUser) {
        List<RoundTestDTO> roundTestDTOList = roundTestService.findByIdCompetition(competitionDTO.getId());
        List<CustomProcessCompeDTO> customProcessCompeDTOs = new ArrayList<>();
        List<Candidate> candidates = new ArrayList<>();
        for (RoundTestDTO roundTestDTO : roundTestDTOList) {
            CustomProcessCompeDTO customProcessCompeDTO = new CustomProcessCompeDTO();
            candidates = candidateRepository.findListByIdUser(idUser, roundTestDTO.getId());
            if(candidates.size()>0){
                Candidate candidate = (Candidate) filterCandidate(candidates)[0];
                customProcessCompeDTO.setResult(candidate.getResult());
                customProcessCompeDTO.setTotalQuestion(Integer.parseInt(filterCandidate(candidates)[1].toString()));
                customProcessCompeDTO.setPoint(candidate.getPoint());
                customProcessCompeDTO.setStatus(candidate.getStatus());
                customProcessCompeDTO.setConfirm(candidate.getConfirm());
                customProcessCompeDTO.setSumPoint(candidate.getSumPoint());
            }else {
                customProcessCompeDTO.setStatus(3);
            }
            customProcessCompeDTO.setConfiged(
                    roundTestDTO.getCodeRoundTest()!=null?true:false);
            customProcessCompeDTOs.add(customProcessCompeDTO);
        }
        competitionDTO.setCustomProcessCompeDTOs(customProcessCompeDTOs);
    }

    private Object[] filterCandidate(List<Candidate> candidate) {
        Integer max=-1000;
        Integer index=0;
        Integer TotalQuestion =0;
        try {
            for (int i=0;i<candidate.size();i++){
                if(max<candidate.get(i).getResult()){
                    max = candidate.get(i).getResult();
                    index=i;
                }
            }
            TotalQuestion = candidate.get(index).getTotalQuestion();
        }catch (Exception e){
            return  new Object[]{candidate.get(index), TotalQuestion};
        }
        return  new Object[]{candidate.get(index), TotalQuestion};
    }



    public StatisticalMyCompetitionDTO statisticalsMyCompetition(){
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Double percentMediumQuestion = setPercentMediumQuestion(user.getId());
        Double percentMediumPoint = setPercentMediumPoint(user.getId());
        // Integer  c = roundTestRepository.countQualifiedByidUser(user.getId());
        return new StatisticalMyCompetitionDTO(roundTestRepository.countQualifiedByidUser(user.getId()),
                roundTestRepository.countImqualifiedByidUser(user.getId()),
                roundTestRepository.countNotDoExamByidUser(user.getId()),0,
                percentMediumQuestion,percentMediumPoint,roundTestRepository.waitingConfirm(user.getId()));
    }

    private Double setPercentMediumPoint(Long idUser) {
        return   candidateRepository.getPersentPointByIdUser(idUser);
    }

    private Double setPercentMediumQuestion(Long idUser) {
        return candidateRepository.findResultTotalByIdUser(idUser);
    }


}
