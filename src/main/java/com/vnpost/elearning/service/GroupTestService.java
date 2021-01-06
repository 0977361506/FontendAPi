package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.GroupTestConverter;
import com.vnpost.elearning.dto.competition.GroupTestDTO;
import eln.common.core.entities.GroupTest;
import eln.common.core.entities.competition.RoundTest;
import eln.common.core.repository.GroupTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupTestService {
    @Autowired
    private GroupTestRepository groupTestRepository;
    @Autowired
    private GroupTestConverter groupTestConverter;
    @Autowired
    private CandidateService candidateService;


    public List<GroupTestDTO> findByIdRound(Long id_round_test) {
        List<GroupTest> list = groupTestRepository.findByIdRound(id_round_test);
        List<GroupTestDTO> groupTestDTOList = new ArrayList<>();
        for (GroupTest a : list) {
            GroupTestDTO groupTestDTO = groupTestConverter.convertToDTO(a);
            groupTestDTOList.add(groupTestDTO);
        }


        return groupTestDTOList;
    }


    public String save(String name, String idround) {


        if (groupTestRepository.countByNameGroup(name) == 0) {
            GroupTest groupTest = new GroupTest();
            RoundTest gRoundTest = new RoundTest();
            gRoundTest.setId(Long.parseLong(idround + ""));
            groupTest.setNameGroup(name);
            groupTest.setRoundTest(gRoundTest);
            groupTestRepository.save(groupTest);
            return "0";
        }

        return "1";
    }


    public String update(String name, String id) {
        if (groupTestRepository.countByNameGroup(name) == 0) {
            GroupTest groupTest = groupTestRepository.findById(Long.parseLong(id + "")).get();
            groupTest.setNameGroup(name);
            groupTestRepository.save(groupTest);
            return "0";
        }

        return "1";
    }


    public String delete(String idGroup, String idRound) {
        if (candidateService.countByIdRoundAndIdGroup(idGroup, idRound) == 0) {
            groupTestRepository.delete(groupTestRepository.findById(Long.parseLong(idGroup + "")).get());
            return "0";
        }
        return "1";
    }


}
