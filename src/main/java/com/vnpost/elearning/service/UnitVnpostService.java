package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.UnitVnpostConverter;
import com.vnpost.elearning.dto.UnitVnpostDTO;
import eln.common.core.repository.UnitVnpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitVnpostService {

    @Autowired
    private UnitVnpostConverter unitVnpostConverter;
    @Autowired
    private UnitVnpostRepository unitVnpostRepository;


    public UnitVnpostDTO findById(String idParent) {
        return unitVnpostConverter.convertToDTO(unitVnpostRepository.findByIdUnit(idParent));
    }


    public UnitVnpostDTO findByIdParent(String idParent) {
        return unitVnpostConverter.convertToDTO(unitVnpostRepository.findByIdParent(idParent));
    }
}
