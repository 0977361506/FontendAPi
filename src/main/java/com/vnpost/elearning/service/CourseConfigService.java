package com.vnpost.elearning.service;

import eln.common.core.entities.course.CourseConfig;
import eln.common.core.repository.course.CourseConfigRepository;
import org.springframework.stereotype.Service;

@Service

public class CourseConfigService extends CommonRepository<CourseConfig,CourseConfigRepository> {

    public CourseConfigService(CourseConfigRepository repo) {
        super(repo);
    }

//    public CourseConfig findByCourseId(Long courseId){
//        return repo.findByCourseId(courseId);
//    }


    public CourseConfig findByCourseId(Long courseId){
        return repo.findByCourseId(courseId);
    }

}
