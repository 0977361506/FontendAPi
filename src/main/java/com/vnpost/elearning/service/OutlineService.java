package com.vnpost.elearning.service;

import eln.common.core.entities.course.Outline;
import eln.common.core.repository.course.OutlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutlineService {
    @Autowired
    private OutlineRepository outlineRepository;




    public Outline findByChapterId(Long chapterId) {
        return outlineRepository.findByChapterId(chapterId);
    }
    public Long getCourseIdByid(Long idOutline)
    {
        return outlineRepository.getIdCourseById(idOutline);
    }


    public Outline findByCourseId(Long courseId) {
        return null;
    }

}
