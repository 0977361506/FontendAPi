package com.vnpost.elearning.service;

import eln.common.core.entities.config.Configurationn;
import eln.common.core.repository.ConfigurationnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationnService {

    @Autowired
    private ConfigurationnRepository configurationnRepository;


    public Configurationn save(Configurationn config) {
        return configurationnRepository.save(config);
    }


    public List<Configurationn> findAll() {
        return configurationnRepository.findAll();
    }


    public Configurationn findById(Long id) {
        return configurationnRepository.findById(id).get();
    }


    public List<Configurationn> findByCodeNameContaining(String codeName) {
        return configurationnRepository.findByCodeNameContaining(codeName);
    }


    public void delete(Long id) {
        configurationnRepository.deleteById(id);
    }

    public Configurationn findByCodeNameAndStatus(String codeName) {
        return configurationnRepository.findByCodeNameAndStatusConfigurationn(codeName,1);
    }
}