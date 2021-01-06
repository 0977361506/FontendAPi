package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.AddPointConverter;
import com.vnpost.elearning.dto.competition.AddPointDTO;
import eln.common.core.entities.competition.AddPoint;
import eln.common.core.entities.question.Levell;
import eln.common.core.entities.competition.RoundTest;
import eln.common.core.repository.AddPointRepository;
import eln.common.core.repository.LevelRepository;
import eln.common.core.repository.RoundTestRepository;
import eln.common.core.repository.TypeQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddPointService {
    @Autowired
    private AddPointRepository addPointRepository;
    @Autowired
    private AddPointConverter addPointConverter;
    @Autowired
    private TypeQuestionRepository typeQuestionRepository;
    @Autowired
    private RoundTestRepository roundTestRepository;
    @Autowired
    private LevelRepository levelRepository;


    public void save(AddPointDTO addPointDTO, String listSub[], long id_roundtest) {

        List<AddPoint> list = addPointRepository.findByIdRoundTest(id_roundtest + "");
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setAddPoint(Integer.parseInt(listSub[i]));
                list.get(i).setLastUpdate(java.util.Calendar.getInstance().getTime());
                addPointRepository.save(list.get(i));

            }
        } else {
            RoundTest roundTest = roundTestRepository.findById(id_roundtest).get();

            for (int i = 0; i < listSub.length; i++) {
                Levell levell = levelRepository.findById(Long.parseLong((i + 1) + "")).get();
                AddPoint addPoint = new AddPoint();
                addPoint.setLastUpdate(java.util.Calendar.getInstance().getTime());
                addPoint.setTimeCreate(java.util.Calendar.getInstance().getTime());
                addPoint.setAddPoint(Integer.parseInt(listSub[i]));
                addPoint.setRoundTest(roundTest);

                addPoint.setLevells(levell);

                addPointRepository.save(addPoint);
            }
        }


    }


    public List<AddPointDTO> findByIdRoundTest(String id_round_test) {
        try {


            List<AddPoint> list = addPointRepository.findByIdRoundTest(id_round_test);
            List<AddPointDTO> subPointDTOList = new ArrayList<>();
            for (AddPoint a : list) {
                AddPointDTO addPointDTO = addPointConverter.convertToDTO(a);
                subPointDTOList.add(addPointDTO);
            }

            return subPointDTOList;
        } catch (Exception e) {
            return null;
        }
    }
}
