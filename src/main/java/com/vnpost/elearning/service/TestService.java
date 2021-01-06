package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.TestConverter;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.dto.competition.TestDTO;
import eln.common.core.entities.competition.Test;
import eln.common.core.repository.QuestionTestRepository;
import eln.common.core.repository.StructDetailTestRepository;
import eln.common.core.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestConverter testConverter;

    @Autowired
    private StructDetailTestRepository structDetailTestRepository;

    @Autowired
    private QuestionTestRepository questionTestRepository;


    public List<TestDTO> findByIdTestKit(String id_test_kit) {
        try {
            List<Test> list = testRepository.findByIdTestKit(id_test_kit);
            List<TestDTO> dtos = new ArrayList<>();
            for (Test entity : list) {
                TestDTO toDTO = testConverter.convertToDTO(entity);
                dtos.add(toDTO);
            }
            return dtos;
        } catch (Exception e) {
            return null;
        }
    }


    public TestDTO findById(long id) {
        try {

            TestDTO test = testConverter.convertToDTO(testRepository.findById(id).get());
            return test;
        } catch (Exception e) {
            return null;
        }
    }


    public TestDTO findByIdStructTest(RoundTestDTO roundTestDTO) {
        try {
            TestDTO test = testConverter.convertToDTO(testRepository.findByIdStructTest(roundTestDTO.getStructTest().getId() + ""));
            return test;
        } catch (Exception e) {
            return null;
        }

    }




}
