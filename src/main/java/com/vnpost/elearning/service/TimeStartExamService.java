package com.vnpost.elearning.service;


import eln.common.core.entities.TimeStartExam;
import eln.common.core.repository.TimeStartExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeStartExamService {
    @Autowired
    private TimeStartExamRepository timeStartExamRepository;

    public TimeStartExam saveTime(TimeStartExam timeStartExam){
        return timeStartExamRepository.save(timeStartExam);
    }
    public Date findByidRoundidUserCount(Long idRound,Long idUser,Integer countTest){
        return timeStartExamRepository.findByidRoundidUserCount(idUser,idRound,countTest);
    }

    public void deleteByidRondIdUserCount(Long idRound,Long idUser,Integer countTest){
         timeStartExamRepository.deleteByIdRoundTestAndIdUserAndCountTest(idRound,idUser,countTest);
    }
}
