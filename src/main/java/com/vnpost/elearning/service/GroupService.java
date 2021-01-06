package com.vnpost.elearning.service;

import eln.common.core.entities.user.Group;
import eln.common.core.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public List<Group> findByType(String type) {
        return groupRepository.findByType(type);
    }


    public Group findById(Long id) {
        return groupRepository.findById(id).get();
    }
}
