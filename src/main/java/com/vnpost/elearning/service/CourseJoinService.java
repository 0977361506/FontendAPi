package com.vnpost.elearning.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.vnpost.elearning.converter.CourseConverter;
import com.vnpost.elearning.converter.CourseJoinConverter;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.CourseJoin;
import eln.common.core.entities.course.QCourseJoin;
import eln.common.core.repository.RoundTestRepository;
import eln.common.core.repository.competition.CompetitionRepository;
import eln.common.core.repository.course.CourseJoinRepository;
import eln.common.core.repository.course.CourseRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Service
public class CourseJoinService extends CommonRepository<CourseJoin, CourseJoinRepository> {

  @Autowired EntityManagerFactory entityManagerFactory;
  private final QCourseJoin Q = QCourseJoin.courseJoin;
  @Autowired private CourseJoinRepository courseJoinRepository;
  @Autowired private CourseJoinConverter courseJoinConverter;
  @Autowired private CourseConverter courseConverter;
  @Autowired private CourseRepository courseRepository;
  @Autowired private RoundTestRepository roundTestRepository;
  @Autowired private CompetitionRepository competitionRepository;

  public CourseJoinService(CourseJoinRepository repo) {
    super(repo);
  }

  @Autowired EntityManager entityManager;

  public List<Course> getListCourseComplatedFromCourseJoin(Long idu, Integer finished) {
    String hql = "select course from CourseJoin as c where c.finished =:f and c.user.id=:idu";
    List<Course> courses =
        entityManager
            .createQuery(hql)
            .setParameter("f", finished)
            .setParameter("idu", idu)
            .getResultList();
    return courses;
  }

  public List<Course> getListByCourseJion(Long id) {
    Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
    String hqlString = "select course from CourseJoin c where c.user.id=:cid";
    Query query = session.createQuery(hqlString);
    query.setParameter("cid", id);
    List<Course> list = query.getResultList();
    return list;
  }

  public List<Course> getListByCourseJionBySearchKey(Long id, String key) {
    Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
    String hqlString =
        "select course from CourseJoin c where c.user.id=:cid and c.course.name like :key";
    Query query = session.createQuery(hqlString);
    query.setParameter("cid", id);
    query.setParameter("key", "%" + key + "%");
    List<Course> list = query.getResultList();
    return list;
  }

  public Integer CheckRoleUseCourse(Long idc, Long idu) {
    String hql = "from CourseJoin c where c.user.id=:idu and c.course.id=:idc";
    List<CourseJoin> courseJoins =
        em.createQuery(hql).setParameter("idc", idc).setParameter("idu", idu).getResultList();
    if (courseJoins.size() == 0) return 0;
    return 1;
  }

  public int getListByCourseJionByUser(Long id, Long idc) {
    Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
    String hqlString = " from CourseJoin c where c.user.id=:cid and c.course.id=:idc ";
    Query query = session.createQuery(hqlString);
    query.setParameter("cid", id);
    query.setParameter("idc", idc);
    List<Course> list = query.getResultList();
    return list.size();
  }

  public Boolean existUserInCourse(Long userId, Long courseId) {
    return repo.existsByCourseIdAndUserId(courseId, userId);
  }

  public Boolean existsByIdCourseAndIdUser(Long userId, Long courseId) {
    return repo.existsByCourseIdAndUserId(courseId, userId);
  }

  public CourseJoin findByCourseIdAndUserId(Long courseId, Long userId) {
    return repo.findByCourseIdAndUserId(courseId, userId);
  }

  @Transactional
  public void updateJoin(Long id) {
    JPAUpdateClause updateClause = new JPAUpdateClause(em, Q);
    updateClause.set(Q.join, SystemConstant.ENABLE).where(Q.id.eq(id)).execute();
  }

  @Transactional
  public void updateFinish(Long userId, Long courseId) {
    if (courseId == null) {
      return;
    }
    CourseJoin courseJoin = repo.findByCourseIdAndUserId(courseId, userId);
    JPAUpdateClause update = new JPAUpdateClause(em, Q);
    update.set(Q.finished, 1);
    update.set(Q.progress,100F);
    update.where(Q.id.eq(courseJoin.getId())).execute();
  }

  public List<Long> getCourseIdsByUserId(Long userId) {
    JPAQuery<CourseJoin> query = new JPAQuery<>(em);
    return query.select(Q.course.id).from(Q).where(Q.user.id.eq(userId)).fetch();
  }

  public List<Long> getCourseIdsByUserIdAndJoinStatus(Long userId, Integer joinStatus) {
    JPAQuery<CourseJoin> query = new JPAQuery<>(em);
    return query
        .select(Q.course.id)
        .from(Q)
        .where(Q.user.id.eq(userId).and(Q.join.eq(joinStatus)))
        .fetch();
  }
}
