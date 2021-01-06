package com.vnpost.elearning.service;


import com.vnpost.elearning.converter.TestKitConverter;
import com.vnpost.elearning.dto.competition.TestkitDTO;
import eln.common.core.entities.competition.TestKit;
import eln.common.core.repository.TestKitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class TestKitService {
    @Autowired
    private TestKitRepository testKitRepository;
    @Autowired
    private TestKitConverter testKitConverter;


    public List<TestkitDTO> findAll() {
        List<TestKit> list = testKitRepository.findAll();
        List<TestkitDTO> dtos = new ArrayList<>();
        for (TestKit testKit : list) {
            TestkitDTO toDTO = testKitConverter.convertToDTO(testKit);
            dtos.add(toDTO);
        }
        return dtos;
    }


    public Integer findByIdTest(String idTest) {
        return null;
    }


    public String save(TestkitDTO testkitDTO) {
        Integer count = testKitRepository.countByName(testkitDTO.getNameTest());
        if (count == 0) {
            testkitDTO.setStatus(0);
            testkitDTO.setTimeCreate(Calendar.getInstance().getTime());
            testkitDTO.setLastUpdate(Calendar.getInstance().getTime());
            TestKit testKit = testKitConverter.convertToEntity(testkitDTO);
            testKitRepository.save(testKit);

            return "0";
        }
        return "1";
    }

    public String update(TestkitDTO testkitDTO) {
        Integer count = testKitRepository.countByName(testkitDTO.getNameTest());
        if (count <= 1) {
            TestKit testKit = testKitRepository.findById(testkitDTO.getId()).get();
            testkitDTO.setStatus(testKit.getStatus());
            testkitDTO.setTimeCreate(testKit.getTimeCreate());
            testkitDTO.setLastUpdate(Calendar.getInstance().getTime());
            TestKit toEntity = testKitConverter.convertToEntity(testkitDTO);
            testKitRepository.save(toEntity);


            return "0";
        }
        return "1";
    }

    public String updateStatus(String[] ids) {
        try {

            for (String id : ids) {
                TestKit testKit = testKitRepository.findById(Long.parseLong(id + "")).get();
                if (testKit.getStatus() == 0) {
                    testKitRepository.updateStatus("1", id);
                } else {
                    testKitRepository.updateStatus("0", id);
                }

            }
        } catch (Exception e) {
            return "1";
        }
        return "0";
    }

}
