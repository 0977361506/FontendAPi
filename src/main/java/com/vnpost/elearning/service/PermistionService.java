package com.vnpost.elearning.service;

import eln.common.core.entities.user.Permistion;
import eln.common.core.repository.user.PermistionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermistionService   {


    private PermistionRepository permistionRepository;


    public Permistion findByCodename(String codeName) {
        return permistionRepository.findByCodename(codeName);
    }


    public Permistion findById(Long id) {
        return permistionRepository.findById(id).get();
    }


    public List<Permistion> findAll() {
        return permistionRepository.findAll();
    }


    public List<Permistion> findAllByParentId(Long parentId) {
        return permistionRepository.findAllByParent(parentId);
    }
}
