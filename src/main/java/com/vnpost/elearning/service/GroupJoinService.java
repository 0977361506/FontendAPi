package com.vnpost.elearning.service;

import eln.common.core.entities.user.GroupJoin;
import eln.common.core.repository.GroupJoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupJoinService {
    @Autowired
    private GroupJoinRepository groupJoinRepository;


    public List<GroupJoin> findByGroupId(Long groupId) {
        return groupJoinRepository.findByIdGroup(groupId);
    }
}
