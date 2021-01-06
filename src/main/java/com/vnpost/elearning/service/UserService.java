package com.vnpost.elearning.service;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.vnpost.elearning.dto.UsersDTO;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.user.QUser;
import eln.common.core.entities.user.User;
import eln.common.core.repository.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends CommonRepository<User, UserRepository> {
  @Autowired CourseService c;
  @Autowired CourseJoinService cj;

  private final QUser Q = QUser.user;

  public UserService(UserRepository repo) {
    super(repo);
  }

  public User findByUsername(String username) {
    return repo.findByUsername(username);
  }

  public Boolean checkUserForRate(Long id) {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // Long aLong = user.getId();
    Course cc = c.findById(id).get();
    //   List<Course> courseJoins = cj.getListByCourseJion(aLong); // khoa hocn ma usẻ tham gia
    // Course course = timkiem(courseJoins, cc.getId()); // kiểm tra xem kháo học này user tham gia
    // chưa
    // Course course = ;
    if (cj.existUserInCourse(user.getId(), cc.getId())) return true;
    return false;
  }
  /*  public Course timkiem(List<Course> list , Long id) {
    for(Course c: list) {
      if(c.getId().equals(id) ) {
        return c;
      }
    }
    return null;
  }*/
  public User findByUserNameAndEmailAndStatus(UsersDTO usersDTO) throws Exception {
    usersDTO.setStatus(1);
    if (StringUtils.isBlank(usersDTO.getUsername())) {
      throw new Exception("No Username");
    }
    if (StringUtils.isBlank(usersDTO.getEmail())) {
      throw new Exception("No Email");
    }
    User entity =
        repo.findByUsernameAndEmailAndStatus(
            usersDTO.getUsername(), usersDTO.getEmail(), usersDTO.getStatus());
    if (entity == null) {
      throw new Exception("Can't find Account");
    }
    return entity;
  }

  public User save(User user) {
    return repo.save(user);
  }

  @Transactional
  public void updatePassword(Long id, String password) {
    JPAUpdateClause update = new JPAUpdateClause(em, Q);
    update.where(Q.id.eq(id)).set(Q.password, password).execute();
  }
}
