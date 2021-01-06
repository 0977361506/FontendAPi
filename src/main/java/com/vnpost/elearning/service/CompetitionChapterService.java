package com.vnpost.elearning.service;

import com.vnpost.elearning.dto.customDTO.StatusExamDTO;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.competition.Candidate;
import eln.common.core.entities.competition.CompetitionChapter;
import eln.common.core.entities.competition.RoundTest;
import eln.common.core.repository.CandidateRepository;
import eln.common.core.repository.RoundTestRepository;
import eln.common.core.repository.competition.CompetitionChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>July 21,2020
 */
@Service
public class CompetitionChapterService extends CommonRepository<CompetitionChapter,CompetitionChapterRepository> {

  public CompetitionChapterService(CompetitionChapterRepository repo) {
    super(repo);
  }
  @Autowired
  private CandidateRepository candidateRepository;

  public  Long  findidRoundByidChapter(Long chapterId){
    List<Long> longList = repo.findidRoundByidChapter(chapterId);
    return longList.size()>0?longList.get(0):null;
  }



  public StatusExamDTO setStatusExamDTO(Long longList){
    MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(longList!=null){
      List<Candidate> candidates =  candidateRepository.findListByIdUser(myUsers.getId(),longList);
      if(candidates.size()>0){
        return   setBigerPoint(candidates,longList);
      }else{
        return new StatusExamDTO(longList,null,null,null);
      }
    }
    return new StatusExamDTO(null,null,null,null);
  }



  private StatusExamDTO setBigerPoint(List<Candidate> candidates,Long idRound) {
    Integer index = null;
    Integer pointest = 0;
    Float persent =null;
    Integer count = 1;
    for (Integer i=0;i<candidates.size();i++){
     if(candidates.get(i).getCounttest()!=0){
          if(candidates.get(i).getPoint()>pointest){
            pointest = candidates.get(i).getPoint();
            index = i;
          }
        }
        if(candidates.get(i).getStatus()==0) {
          count=0;
        }

      }
    if(index!=null){
        persent = (float) candidates.get(index).getPoint()/candidates.get(index).getSumPoint();
    }
    return new   StatusExamDTO(idRound,count,pointest,persent);
  }



  public Integer countByChapterId(Long chapterId){
    return repo.countByChapterId(chapterId);
  }

  public Optional<CompetitionChapter> findByChapterId(Long chapterId){
    return repo.findByChapterId(chapterId);
  }


}
