package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import eln.common.core.entities.chapter.ChapterCourseWare;
import eln.common.core.entities.chapter.QChapterCourseWare;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.repository.course.ChapterCourseWareRepository;
import eln.common.core.repository.course.ChapterRepository;
import eln.common.core.repository.course.OutlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class ChapterCourseWareService
    extends CommonRepository<ChapterCourseWare, ChapterCourseWareRepository> {
  @Autowired EntityManagerFactory entityManagerFactory;
  @Autowired private OutlineRepository outlineRepository;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private ChapterCourseWareRepository chapterCourseWareRepository;

  private final QChapterCourseWare Q = QChapterCourseWare.chapterCourseWare;

  public ChapterCourseWareService(ChapterCourseWareRepository repo) {
    super(repo);
  }

  public List<CourseWare> getListCourseWareFromChapterCourseWare(Long id) {

    String hqString = "select c.courseWare from ChapterCourseWare  c where c.chapter.id=:cid";
    List<CourseWare> list = em.createQuery(hqString).setParameter("cid", id).getResultList();
    return list;
  }

  public int getTotalListCourseWareFromChapterCourseWare(Long id) {
    String hqString =
        "select count(c.courseWare) from ChapterCourseWare  c where c.chapter.id=:cid";
    int sl =
        Integer.parseInt(
            em.createQuery(hqString).setParameter("cid", id).getSingleResult().toString());
    return sl;
  }

  public int getListCourseWareFromChapterCourseWareAndStatus(Long id, Long idu, int status) {
    String hqString =
        "select count(c.courseWare) from CourseWareProcess  c where c.chapter.id=:cid and c.user.id=:idu and c.status=:check";
    int soluongCourseWareByUser =
        Integer.parseInt(
            em.createQuery(hqString)
                .setParameter("cid", id)
                .setParameter("idu", idu)
                .setParameter("check", status)
                .getSingleResult()
                .toString());
    if (soluongCourseWareByUser > 0) return soluongCourseWareByUser;
    return 0;
  }

  public Long countAllByListChapterId(List<Long> chapterIds) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.chapter.id.in(chapterIds));
    return count(builder);
  }

  public List<Long> getListCourseIdByChapterId(Long idChapter) {
    return repo.getIdCourseById(idChapter);
  }

  public List<ChapterCourseWare> findByChapterId(Long chapterId) {
    return repo.findByChapterId(chapterId);
  }

  public ChapterCourseWare findByChapterIdAndCourseWareId(Long chapterId, Long courseWareId) {
    return repo.findByChapterIdAndCourseWareId(chapterId, courseWareId);
  }

  public Integer countTotalCourseWareInCourse(Long idCourse, List<Long> chapterIds) {
    return chapterCourseWareRepository.countByListIdChapter(chapterIds);
  }

  public Integer countByChapterId(Long chapterId) {
    return chapterCourseWareRepository.countByChapterId(chapterId);
  }
}
