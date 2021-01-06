package com.vnpost.elearning.service;

import eln.common.core.entities.course.CourseCategoryTree;
import eln.common.core.entities.course.Coursecategory;
import eln.common.core.repository.course.CourseCategoryTreeRespository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Service
public class CoursesCategoryTreeService {
    @Autowired
    CourseCategoryTreeRespository c;
    @Autowired
    EntityManagerFactory entityManagerFactory;

    public List<Coursecategory> findAll() {
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);

        String qString = "select  distinct  c.courseCategoryOne.id from CourseCategoryTree c";
        Query query = session.createQuery(qString);
        List<Coursecategory> list = query.getResultList();
        return list;
    }

    public List<CourseCategoryTree> findCategoryByid(Long id) {
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);

        String qString = "from CourseCategoryTree c where c.courseCategoryOne.id=:cid ";
        Query query = session.createQuery(qString);
        query.setParameter("cid", id);
        List<CourseCategoryTree> list = query.getResultList();
        if (list != null) return list;

        return null;
    }

    public long count() {
        return c.count();
    }

}
