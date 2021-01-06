package com.vnpost.elearning.service;

import eln.common.core.entities.course.CourseRole;
import eln.common.core.repository.course.CourseRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Nguyen Anh Tuan
 * <p>
 * May 02, 2020
 */
@Service
public class CourseRoleService {
    @Autowired
    private CourseRoleRepository courseRoleRepository;

    public CourseRole findById(Long id) {
        return courseRoleRepository.findById(id).get();
    }

    public List<CourseRole> findAll() {
        return courseRoleRepository.findAll();
    }
}
