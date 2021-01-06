package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.PoscodeVnpostConverter;
import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import eln.common.core.entities.unit.PoscodeVnpost;
import eln.common.core.repository.PoscodeVnpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class PoscodeVnpostService {
    @Autowired
    private PoscodeVnpostRepository poscodeVnpostRepository;
    @Autowired
    private PoscodeVnpostConverter poscodeVnpostConverter;
    @Autowired
    EntityManager e;


    public PoscodeVnpostDTO findById(String id) {
        PoscodeVnpost poscodeVnpost = poscodeVnpostRepository.findByIdPos(id);
        PoscodeVnpostDTO poscodeVnpostDTO = poscodeVnpostConverter.convertToDTO(poscodeVnpost);

        return poscodeVnpostDTO;
    }
    public PoscodeVnpost findByIdEntity(String id) {
        PoscodeVnpost poscodeVnpost = poscodeVnpostRepository.findByIdPos(id);

        return poscodeVnpost;
    }

    public PoscodeVnpost findOne(String id) {
        return poscodeVnpostRepository.findByIdPos(id);
    }

}
