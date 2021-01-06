package com.vnpost.elearning.service;

import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.repository.CourseWareRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseWareService extends CommonRepository<CourseWare, CourseWareRepository> {


  public CourseWareService(CourseWareRepository repo) {
    super(repo);
  }

  public List<CourseWare> findByChapterId(Long chapterId) {

    return repo.findByChapterId(chapterId);
  }
}
