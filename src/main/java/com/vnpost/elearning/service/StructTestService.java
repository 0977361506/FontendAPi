package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.StructTestConverter;
import com.vnpost.elearning.dto.competition.StructTestDTO;
import eln.common.core.repository.StructTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StructTestService {
    @Autowired
    private StructTestRepository structTestRepository;
    @Autowired
    private StructTestConverter structTestConverter;


    public StructTestDTO save(StructTestDTO structTestDTO) {
        structTestDTO.setTimeCreate(java.util.Calendar.getInstance().getTime());
        structTestDTO.setLastUpdate(java.util.Calendar.getInstance().getTime());

        StructTestDTO structTestDTO1 = structTestConverter.convertToDTO(
                structTestRepository.save(structTestConverter.convertToEntity(structTestDTO)));


        return structTestDTO1;

    }
}
