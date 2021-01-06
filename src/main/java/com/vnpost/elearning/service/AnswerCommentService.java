package com.vnpost.elearning.service;

import eln.common.core.entities.course.AnswerComment;
import eln.common.core.repository.AnswerCommentRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class AnswerCommentService extends CommonRepository<AnswerComment, AnswerCommentRespository>{
    @Autowired
    AnswerCommentRespository cm;
    @Autowired
    EntityManagerFactory en;

    public AnswerCommentService(AnswerCommentRespository repo) {
        super(repo);
    }

    public AnswerComment save(AnswerComment entity) {
        return cm.save(entity);
    }


//    public AnswerComment findById(Long id) {
//        Session session = en.createEntityManager().unwrap(Session.class);
//        AnswerComment aNew = session.find(AnswerComment.class, id);
//        return aNew;
//    }

    public List<AnswerComment> findAll() {
        return (List<AnswerComment>) cm.findAll();
    }


    public Long count() {
        return cm.count();
    }

}
