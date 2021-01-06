package com.vnpost.elearning.service;

import com.vnpost.elearning.dto.PrioritizeDTO;
import eln.common.core.entities.Prioritize;
import eln.common.core.repository.PrioritizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorotizeService {

    @Autowired
    private PrioritizeRepository prioritizeRepository;


    public List<PrioritizeDTO> findAll() {
        return null;
    }


    public List<Prioritize> findAll2() {
        return prioritizeRepository.findAll();
    }
}
