package com.vnpost.elearning.service;

import eln.common.core.entities.config.HelpDesk;
import eln.common.core.repository.HelpDeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpDeskService {
    @Autowired
    HelpDeskRepository hel;

    public List<HelpDesk> findAll() {
        return hel.findAll();
    }

    public HelpDesk save(HelpDesk entity) {
        return hel.save(entity);
    }


}
