package com.vnpost.elearning.processor;

import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CourseConfigService;
import com.vnpost.elearning.service.CourseJoinRequestService;
import com.vnpost.elearning.service.CourseJoinService;
import com.vnpost.elearning.service.CourseService;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.CourseConfig;
import eln.common.core.entities.course.CourseJoinRequest;
import eln.common.core.entities.course.QCourseJoinRequest;
import eln.common.core.exception.CourseRequestException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author:Nguyen Anh Tuan
 *     <p>May 30,2020
 */
@Service
@AllArgsConstructor
public class CourseJoinRequestProcessor {
  private CourseJoinRequestService courseJoinRequestService;
  private CourseService courseService;
  private CourseConfigService courseConfigService;
  private CourseJoinService courseJoinService;
  private final QCourseJoinRequest Q = QCourseJoinRequest.courseJoinRequest;

  public void createRequest(Long courseId) throws CourseRequestException {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    validRequest(courseId, myUser);
    CourseJoinRequest courseJoinRequest = setupRequest(myUser.getPosCode(), courseId);
    courseJoinRequest.setIdUser(myUser.getId());
    courseJoinRequestService.save(courseJoinRequest);
  }

  private void validRequest(Long courseId, MyUser myUser) throws CourseRequestException {
    Optional<Course> courseOp = courseService.findById(courseId);
    if (!courseOp.isPresent()) {
      throw new CourseRequestException("Không tìm thấy khóa học");
    }
    if (courseOp.get().getStatus() == SystemConstant.DISABLE) {
      throw new CourseRequestException("Khóa học đã bị khóa");
    }
    if (courseJoinService.existUserInCourse(myUser.getId(), courseId)) {
      throw new CourseRequestException("Đã có trong khóa học");
    }
    Boolean checkExitsInRequest =
        courseJoinRequestService.exitsUserAndCourseId(myUser.getId(), courseId);
    if (checkExitsInRequest) {
      throw new CourseRequestException("Đã có trong danh sách duyệt");
    }
    CourseConfig courseConfig = courseConfigService.findByCourseId(courseId);
    if (validRegister(courseConfig)) {
      throw new CourseRequestException("Khóa học không cần đăng ký");
    }
    if (!validDateRegister(courseConfig)) {
      throw new CourseRequestException("Thời gian đăng ký đã kết thúc");
    }
  }

  private CourseJoinRequest setupRequest(String poscode, Long courseId) {
    CourseJoinRequest courseJoinRequest = new CourseJoinRequest();
    if (StringUtils.isNotBlank(poscode)) {
      courseJoinRequest.setIdPoscode(Long.valueOf(poscode));
    }
    courseJoinRequest.setIdCourse(courseId);
    return courseJoinRequest;
  }

  private Boolean validRegister(CourseConfig courseConfig) {
    return courseConfig.getFreedomRegister() == 0 || courseConfig.getFreedomRegister() == null;
  }

  private Boolean validDateRegister(CourseConfig courseConfig) {
    Date current = new Date();
    Date registerStart = courseConfig.getRegisterStart();
    Date registerEnd = new Date(courseConfig.getRegisterEnd().getTime() + SystemConstant.ONE_DAY);

    return current.after(registerStart) && current.before(registerEnd);
  }

  private Boolean validFreeJoin(CourseConfig courseConfig) {
    return courseConfig.getApproveAuto() == 1;
  }
}
