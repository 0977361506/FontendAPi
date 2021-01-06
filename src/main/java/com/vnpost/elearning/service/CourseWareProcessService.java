package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAUpdateClause;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.chapter.ChapterCourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.courseware.QCourseWareProcess;
import eln.common.core.repository.CourseWareProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseWareProcessService
    extends CommonRepository<CourseWareProcess, CourseWareProcessRepository> {
  private final QCourseWareProcess Q = QCourseWareProcess.courseWareProcess;

  public CourseWareProcessService(CourseWareProcessRepository repo) {
    super(repo);
  }

  public List<CourseWareProcess> findAllCourseFromCourseWareProcess(
      Long idu, Long idcw, int a, Long idct) {
    String hqlString =
        "from CourseWareProcess c where c.courseWare.id=:idcw  and c.user.id=:idu and c.status=:a and c.chapter.id=:idct";
    return em.createQuery(hqlString)
        .setParameter("idu", idu)
        .setParameter("idcw", idcw)
        .setParameter("a", a)
        .setParameter("idct", idct)
        .getResultList();
  }

  public Integer countAllCourseFromCourseWareProcess(Long idu, Long idcw, int a, Long idct) {
    String hqlString =
        "select count (*) from CourseWareProcess c where c.courseWare.id=:idcw  and c.user.id=:idu and c.status=:a and c.chapter.id=:idct";
    return ((Long)
            em.createQuery(hqlString)
                .setParameter("idu", idu)
                .setParameter("idcw", idcw)
                .setParameter("a", a)
                .setParameter("idct", idct)
                .getSingleResult())
        .intValue();
  }

  public boolean exitsUserAndCourseWare(Long userId, Long courseWareId, Long chapterId) {
    return repo.existsByUserIdAndCourseWareIdAndChapterId(userId, courseWareId, chapterId);
  }

  public boolean exitsUserAndCourseWareAndStatus(
      Long userId, Long courseWareId, Long chapterId, int status) {
    return repo.existsByUserIdAndCourseWareIdAndChapterIdAndStatus(
        userId, courseWareId, chapterId, status);
  }

  public Integer countByUserAndCourseWareAndStatus(Long userId, Long chapterId, Integer status) {
    return repo.countByUserIdAndChapterIdAndStatus(userId, chapterId, status);
  }

  public Integer countByUserAndIdChaptersAndStatus(
      Long userId, List<Long> chapterIds, Integer status) {
    return repo.countByUserIdAndIdChaptersIdAndStatus(userId, chapterIds, status);
  }




  public Optional<CourseWareProcess> findByUserIdAndCourseWareIdAndChapterId(
      Long userId, Long courseWareId, Long chapterId) {
    return repo.findByUserIdAndCourseWareIdAndChapterId(userId, courseWareId, chapterId);
  }

  public Optional<CourseWareProcess> findByChapterCourseWareAndUser(
      ChapterCourseWare chapterCourseWare, Long userId) {
    return repo.findByUserIdAndCourseWareIdAndChapterId(
        userId, chapterCourseWare.getCourseWare().getId(), chapterCourseWare.getChapter().getId());
  }

  public Long countByChapterIdsAndUserId(List<Long> chapterIds, Long userId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.status.eq(SystemConstant.ENABLE));
    builder.and(Q.user.id.eq(userId)).and(Q.chapter.id.in(chapterIds));
    return count(builder);
  }

  @Transactional
  public void updateView(Long userId, Long courseWareId, Long chapterId) {
    Optional<CourseWareProcess> courseWareProcessOp =
        this.findByUserIdAndCourseWareIdAndChapterId(userId, courseWareId, chapterId);
    if (!courseWareProcessOp.isPresent()) {
      return;
    }
    CourseWareProcess courseWareProcess = courseWareProcessOp.get();
    JPAUpdateClause updateClause = new JPAUpdateClause(em, Q);
    updateClause
        //        .set(Q.status, SystemConstant.ENABLE)
        .set(Q.totalView, courseWareProcess.getTotalView() + 1)
        .set(Q.modifiedDate, new Date())
        .set(Q.lastView, courseWareProcess.getLastView() + 1)
        .where(Q.id.eq(courseWareProcess.getId()))
        .execute();
  }
  public CourseWareProcess getCourseWareProcess(Long courseWareId,Long chapterId,Long userId ) {
    return repo.findByCourseWareIdAndChapterIdAndUserId(courseWareId,chapterId,userId);
  }

   public List<CourseWareProcess> findByIduIdChapterId(Long idUser, Long chapterId){
      return   repo.findByIduIdChapterId(idUser, chapterId);
  }
}
