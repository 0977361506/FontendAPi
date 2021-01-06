package com.vnpost.elearning.service;

import eln.common.core.entities.config.StaticPage;
import eln.common.core.repository.StaticPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticPageService {

    @Autowired
    private StaticPageRepository staticPageRepository;


    public List<StaticPage> findAll() {
        return staticPageRepository.findAll();
    }


    public StaticPage save(StaticPage entity) {
        return staticPageRepository.save(entity);
    }


    public StaticPage findById(Long id) {
        return staticPageRepository.findById(id).get();
    }


    public void delete(StaticPage staticPage) {
        staticPageRepository.delete(staticPage);
    }

    public StaticPage findByCode(String code) {
        return staticPageRepository.findByCode(code);
    }
}
