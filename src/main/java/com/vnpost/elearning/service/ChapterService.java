package com.vnpost.elearning.service;

import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.chapter.QChapter;
import eln.common.core.repository.course.ChapterRepository;
import eln.common.core.repository.course.OutlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChapterService extends CommonRepository<Chapter, ChapterRepository> {
  public ChapterService(ChapterRepository repo) {
    super(repo);
  }

  @Autowired private OutlineRepository outlineRepository;

  private final QChapter Q = QChapter.chapter;

  @CacheEvict(value = "chapterIds", key = "#courseId")
  public List<Long> findIdsChapterByCourseId(Long courseId) {
    return repo.findByCourseId(courseId).stream().map(Chapter::getId).collect(Collectors.toList());
  }

  public List<Chapter> findByCourseId(Long courseId) {
    return repo.findByCourseId(courseId);
  }

  public List<Long> findListIdByOutlineId(Long courseId) {
    return repo.findListIdByOutlineId(outlineRepository.findByCourseId(courseId).getId());
  }

  public Long  getIdOutlineById(Long idChapter){
    return repo.getIdOutlineById(idChapter);
  }
}
