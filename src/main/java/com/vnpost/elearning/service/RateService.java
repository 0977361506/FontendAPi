package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.RateConverter;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.Rate;
import eln.common.core.repository.RateRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class RateService extends CommonRepository<Rate, RateRepository> {
    @Autowired
    EntityManager entityManager;
    @Autowired
    EntityManagerFactory entityManagerFactory;
    @Autowired
    private RateConverter converter;
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private UserService userService;

    public RateService(RateRepository repo) {
        super(repo);
    }

    public List<Rate> findByCourseId(Long courseId) {
        return rateRepository.findByCourseId(courseId);
    }

    public Rate CheckUserRated(Long idc, Long idu) {
        String hql = "from Rate r where r.course.id=:idc and r.user.id=:idu";
        List<Rate> rates = entityManager.createQuery(hql).setParameter("idc", idc).setParameter("idu", idu).getResultList();
        if (rates.size() > 0) return rates.get(0);
        return null;
    }

    public Rate saveRateByUser(Course course, int value) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Rate rate = new Rate();
        rate.setValuess(value);
        rate.setCourse(course);
        rate.setUser(userService.findById(user.getId()).get());
        save(rate);
        return rate;
    }


    public Course getFromCourse(Long id) {

        String hqlString = "select course from Rate c where c.course.id=:cid ";
        List<Course> list = entityManager.createQuery(hqlString).setParameter("cid", id).getResultList();
        for (Course c : list) {
            if (c.getId() == id) return c;
        }
        return null;

    }


    public List<Rate> getRateFromCourse(Long id) {
        String hqlString = "from Rate c where c.course.id=:cid ";
        List<Rate> list = entityManager.createQuery(hqlString).setParameter("cid", id).getResultList();
        return list;
    }


    public Long countRateFromCourse(Long id) {
        String hqlString = "select count(*) from Rate c where c.course.id=:cid ";
       Long count  = (Long) entityManager.createQuery(hqlString).setParameter("cid", id).getSingleResult();
        return count;
    }

    public Long sumRate(Long id) {
        String hqlString = "select sum(valuess) from Rate c where c.course.id=:cid ";
        Long sum = Long.valueOf(0);
        sum  = (Long) entityManager.createQuery(hqlString).setParameter("cid", id).getSingleResult();
        if(sum== null) return Long.valueOf(0);
        return sum;
    }

    public Rate findbyId(Long id) {
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        return session.find(Rate.class, id);

    }


    public Rate sortByid(Long id) {
        String hqString = "from Rate r  where r.course.id=:cid order by r.id desc";
        List<Rate> rates = entityManager.createQuery(hqString).setParameter("cid", id).getResultList();
        Rate rate = new Rate();
        if (rates.size() == 0) {
            rate.setStar_one("width:0%;");
            rate.setStar_two("width:0%;");
            rate.setStar_three("width:0%;");
            rate.setStar_for("width:0%;");
            rate.setStar_five("width:0%;");

        } else {
            rate = rates.get(0);
        }
        return rate;
    }

    public Boolean isUserRated(Long userId, Long courseId) {
        return rateRepository.existsByUserIdAndCourseId(userId,courseId);
    }
}
