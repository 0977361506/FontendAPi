package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.CommentConverter;
import eln.common.core.entities.course.Comment;
import eln.common.core.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class CommentService extends CommonRepository<Comment, CommentRepository> {
  @Autowired CommentConverter converter;
  @Autowired
  EntityManager en;

  public CommentService(CommentRepository repo) {
    super(repo);
  }

  public long getToTalNumberPage(int size,Long idc) {
    String hql = "from Comment c where c.course.id=:idc order by c.createdDate desc";
    List<Comment> list = en.createQuery(hql).setParameter("idc",idc).getResultList();
    long pageCount = (long) Math.ceil(1.0 * (list.size()/ size));
    return pageCount;
  }

  public List<Comment> getCommentByPageNo(int start, int end,Long idc) {
    String hql = "from Comment c where c.course.id=:idc order by c.createdDate desc";
    List<Comment> list =
            en.createQuery(hql).setParameter("idc",idc).setFirstResult(start * end).setMaxResults(end).getResultList();
    return list;
  }

  public List<Comment> findByCourseId(Long courseId) {
    return repo.findByCourseId(courseId);
  }
}
