package com.vnpost.elearning.service;

import eln.common.core.entities.unit.ProvinceVnpost;
import eln.common.core.repository.ProvinceVnpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceVnpostService {

    @Autowired
    private ProvinceVnpostRepository provinceVnpostRepository;

    public List<ProvinceVnpost> findAllProvinceOnly() {
        return provinceVnpostRepository.findAllByIdNot(new Long(8));
    }
}
