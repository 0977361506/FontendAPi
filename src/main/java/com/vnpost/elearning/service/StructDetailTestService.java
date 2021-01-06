package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.StructDetailTestConverter;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.dto.competition.StructDetailTestDTO;
import com.vnpost.elearning.dto.competition.TestDTO;
import eln.common.core.entities.competition.StructDetailTest;
import eln.common.core.repository.StructDetailTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class StructDetailTestService {
    @Autowired
    private StructDetailTestRepository structDetailTestRepository;
    @Autowired
    StructDetailTestConverter structDetailTestConverter;

    public void saveStructDetailTest(StructDetailTestDTO structDetailTestDTO) {
        structDetailTestDTO.setLastUpdate(java.util.Calendar.getInstance().getTime());
        structDetailTestDTO.setTimeCreate(java.util.Calendar.getInstance().getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        String date_update = dateFormat.format(structDetailTestDTO.getLastUpdate());
        String date_create = dateFormat.format(structDetailTestDTO.getTimeCreate());

        structDetailTestRepository.saveStructDetailTest(structDetailTestDTO.getCountTest() + "", date_update,
                structDetailTestDTO.getNameGroup(), date_create, structDetailTestDTO.getIdlevell(),
                structDetailTestDTO.getIdtag(), structDetailTestDTO.getIdtypeQuestion(),
                structDetailTestDTO.getIdquestionCategoryStruct(), structDetailTestDTO.getIdstructTestDetail());
    }


    public List<StructDetailTestDTO> findByIdStructTest(RoundTestDTO roundTestDTO) {
        try {
            List<StructDetailTest> list = structDetailTestRepository.findByIdStructTest(roundTestDTO.getStructTest().getId() + "");
            List<StructDetailTestDTO> structDetailTestDTOList = new ArrayList<>();
            for (StructDetailTest a : list) {
                StructDetailTestDTO subPointDTO = structDetailTestConverter.convertToDTO(a);
                structDetailTestDTOList.add(subPointDTO);
            }

            return structDetailTestDTOList;
        } catch (Exception e) {
            return null;
        }
    }


    public StructDetailTestDTO findById(long id) {
        StructDetailTest structDetailTest = structDetailTestRepository.findById(id).get();
        return structDetailTestConverter.convertToDTO(structDetailTest);
    }


    public List<StructDetailTestDTO> findByIdStructTestTest(TestDTO testDTO) {
        try {
            List<StructDetailTest> list = structDetailTestRepository.findByIdStructTest(testDTO.getStructTest().getId() + "");
            List<StructDetailTestDTO> structDetailTestDTOList = new ArrayList<>();
            for (StructDetailTest a : list) {
                StructDetailTestDTO subPointDTO = structDetailTestConverter.convertToDTO(a);
                structDetailTestDTOList.add(subPointDTO);
            }

            return structDetailTestDTOList;
        } catch (Exception e) {
            return null;
        }
    }

}
