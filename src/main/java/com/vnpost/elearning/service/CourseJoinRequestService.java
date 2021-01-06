package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import eln.common.core.entities.course.CourseJoinRequest;
import eln.common.core.entities.course.QCourseJoinRequest;
import eln.common.core.repository.course.CourseJoinRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>May 30,2020
 */
@Service
public class CourseJoinRequestService
    extends CommonRepository<CourseJoinRequest, CourseJoinRequestRepository> {
  @Autowired
  EntityManager entityManager;
  public CourseJoinRequestService(CourseJoinRequestRepository repo) {
    super(repo);
  }

  private final QCourseJoinRequest Q = QCourseJoinRequest.courseJoinRequest;

  public Boolean exitsUserAndCourseId(Long idUser, Long courseId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.idCourse.eq(courseId));
    builder.and(Q.idUser.eq(idUser));
    return repo.exists(builder);
  }
  @Transactional
  public int deleteRequestByUserAndIdCourse(Long userId, Long courseId){
    String hql = "DELETE  FROM CourseJoinRequest  as c WHERE c.idCourse=:idc and c.idUser=:idu";
    int row = entityManager.createQuery(hql).setParameter("idc",courseId).setParameter("idu",userId).executeUpdate();
    return  row ;
  }
  public void deleteByUserIdAndCourseId(Long userId, Long courseId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.idCourse.eq(courseId));
    builder.and(Q.idUser.eq(userId));
    Optional<CourseJoinRequest> courseJoinRequest = repo.findOne(builder);
    courseJoinRequest.ifPresent(this::delete);
  }

  public List<Long> getCourseIdsByUserId(Long userId) {
    JPAQuery<CourseJoinRequest> query = new JPAQuery<>(em);
    return query.select(Q.idCourse).from(Q).where(Q.idUser.eq(userId)).fetch();
  }
}
