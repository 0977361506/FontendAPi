package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.SubPointConverter;
import com.vnpost.elearning.dto.competition.SubPointDTO;
import eln.common.core.entities.question.Levell;
import eln.common.core.entities.competition.RoundTest;
import eln.common.core.entities.competition.SubPoint;
import eln.common.core.repository.LevelRepository;
import eln.common.core.repository.RoundTestRepository;
import eln.common.core.repository.SubPointRepository;
import eln.common.core.repository.TypeQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubPointService {
    @Autowired
    private SubPointRepository subPointRepository;
    @Autowired
    private SubPointConverter subPointConverter;
    @Autowired
    private TypeQuestionRepository typeQuestionRepository;
    @Autowired
    private RoundTestRepository roundTestRepository;
    @Autowired
    private LevelRepository levelRepository;


    public void save(SubPointDTO subPointDTO, String listSub[], long id_roundtest) {
        List<SubPoint> list = subPointRepository.findByIdRoundTest(id_roundtest + "");
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setSub(Integer.parseInt(listSub[i]));
                list.get(i).setLastUpdate(java.util.Calendar.getInstance().getTime());
                subPointRepository.save(list.get(i));

            }
        } else {
            RoundTest roundTest = roundTestRepository.findById(id_roundtest).get();

            for (int i = 0; i < listSub.length; i++) {
                Levell levell = levelRepository.findById(Long.parseLong((i + 1) + "")).get();
                SubPoint subPoint = new SubPoint();
                subPoint.setLastUpdate(java.util.Calendar.getInstance().getTime());
                subPoint.setTimeCreate(java.util.Calendar.getInstance().getTime());
                subPoint.setSub(Integer.parseInt(listSub[i]));
                subPoint.setRoundTest(roundTest);


                subPoint.setLevells(levell);

                subPointRepository.save(subPoint);
            }
        }


    }


    public List<SubPointDTO> findByIdRoundTest(String id_round_test) {
        try {


            List<SubPoint> list = subPointRepository.findByIdRoundTest(id_round_test);
            List<SubPointDTO> subPointDTOList = new ArrayList<>();
            for (SubPoint a : list) {
                SubPointDTO subPointDTO = subPointConverter.convertToDTO(a);
                subPointDTOList.add(subPointDTO);
            }

            return subPointDTOList;
        } catch (Exception e) {
            return null;
        }
    }
}
