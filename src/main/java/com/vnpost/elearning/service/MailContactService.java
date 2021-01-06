package com.vnpost.elearning.service;

import eln.common.core.entities.user.MailContact;
import eln.common.core.repository.MailContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailContactService {
    @Autowired
    private MailContactRepository mail;

    public MailContact save(MailContact entity) {
        return mail.save(entity);
    }

    public List<MailContact> findAll() {
        return mail.findAll();
    }


}
