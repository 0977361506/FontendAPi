package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.ChapterConverter;
import com.vnpost.elearning.converter.CourseConverter;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.RateDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.QCourse;
import eln.common.core.entities.course.Rate;
import eln.common.core.exception.CourseException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 05,2020
 */
@Service
@AllArgsConstructor
public class CourseProcessor {
  private final QCourse Q = QCourse.course;
  private CourseService courseService;
  private CourseConverter converter;
  private CourseJoinService courseJoinService;
  private RateService rateService;
  private UserService userService;
  private ChapterService chapterService;
  private CourseWareProcessService courseWareProcessService;
  private CourseCategoryService courseCategoryService;
  private CourseJoinRequestService courseJoinRequestService;
  private CompetitionChapterService competitionChapterService;
  private RoundTestService roundTestService;
  private ChapterConverter chapterConverter;

  public List<CourseDTO> findAll() {
    BooleanBuilder builder = new BooleanBuilder().and(Q.status.eq(SystemConstant.ENABLE));
    Iterable<Course> listCourse = courseService.findAll(builder);
    List<Course> result = new ArrayList<>();
    listCourse.forEach(result::add);
    return result.stream().map(converter::convertToDTO).collect(Collectors.toList());
  }

  public List<CourseDTO> findAll(CourseDTO courseDTO, Pageable pageable) {
    BooleanBuilder builder = commonBuilder(courseDTO);
    return courseService.findAll(builder, pageable).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<CourseDTO> findAllCourseForMyCourse(CourseDTO courseDTO, Pageable pageable) {
    courseDTO.setStatus(SystemConstant.BOTH);
    BooleanBuilder builder = commonBuilder(courseDTO);
    setBuilderForMyCourse(builder, courseDTO);
    return courseService.findAll(builder, pageable).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<CourseDTO> findCourseByCategoryAndChild(CourseDTO courseDTO, Pageable pageable) {
    BooleanBuilder builder = commonBuilder(courseDTO);
    return courseService.findAll(builder, pageable).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public Long countByCategoryAndChild(CourseDTO courseDTO) {
    BooleanBuilder builder = commonBuilder(courseDTO);

    return courseService.count(builder);
  }

  public Long countForMyCourse(CourseDTO courseDTO) {
    courseDTO.setStatus(SystemConstant.BOTH);
    BooleanBuilder builder = commonBuilder(courseDTO);
    setBuilderForMyCourse(builder, courseDTO);
    return courseService.count(builder);
  }

  private void setBuilderForMyCourse(BooleanBuilder builder, CourseDTO courseDTO) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    List<Long> courseIdsWaitAccept = courseJoinRequestService.getCourseIdsByUserId(myUser.getId());
    List<Long> courseIdsWasJoin =
        courseJoinService.getCourseIdsByUserIdAndJoinStatus(myUser.getId(), SystemConstant.JOIN);
    List<Long> courseIdsNotJoin =
        courseJoinService.getCourseIdsByUserIdAndJoinStatus(
            myUser.getId(), SystemConstant.NOT_JOIN);
    if (StringUtils.isNotBlank(courseDTO.getTypeMyCourse())) {
      switch (courseDTO.getTypeMyCourse()) {
        case SystemConstant.MY_COURSE_NOT_JOIN:
          builder.and(Q.id.in(courseIdsNotJoin));
          break;
        case SystemConstant.MY_COURSE_WAIT_ACCEPT:
          builder.and(Q.id.in(courseIdsWaitAccept));
          break;
        case SystemConstant.MY_COURSE_WAS_JOIN:
          builder.and(Q.id.in(courseIdsWasJoin));
          break;
        default:
          builder.and(
              Q.id
                  .in(courseIdsNotJoin)
                  .or(Q.id.in(courseIdsWasJoin)));
          break;
      }
    }
  }

  public Long count(CourseDTO courseDTO) {
    BooleanBuilder builder = commonBuilder(courseDTO);
    return courseService.count(builder);
  }

  public CourseDTO findByChapterId(Long chapterId) {
    return converter.convertToDTO(courseService.findByChapterId(chapterId));
  }

  public CourseDTO findById(Long id) throws CourseException {
    Optional<Course> courseOptional = courseService.findById(id);
    if (!courseOptional.isPresent()) {
      throw new CourseException("Không tìm thấy khóa học");
    }
    Course course = courseOptional.get();
    if (course.getStatus() == SystemConstant.DISABLE) {
      throw new CourseException("Khóa học đã bị khóa");
    }
    return converter.convertToDTO(course);
  }

  public void addRatingToCourse(RateDTO rateDTO) throws Exception {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (rateDTO.getCourseId() == null) throw new Exception("No Course Id");
    Optional<Course> course = courseService.findById(rateDTO.getCourseId());
    if (!course.isPresent()) throw new Exception("Course doesn't exit");
    if (rateDTO.getValuess() == null) throw new Exception("no rating");
    if (!courseJoinService.existUserInCourse(user.getId(), rateDTO.getCourseId()))
      throw new Exception("You are not in course");
    if (rateService.isUserRated(user.getId(), course.get().getId()))
      throw new DataIntegrityViolationException("You are rated");
    Rate rate = new Rate();
    rate.setValuess(rateDTO.getValuess());
    rate.setCourse(course.get());
    rate.setUser(userService.findById(user.getId()).get());
    rateService.save(rate);
  }

  private BooleanBuilder commonBuilder(CourseDTO courseDTO) {
    BooleanBuilder builder = new BooleanBuilder();
    if (courseDTO.getStatus() != null) {
      if (!courseDTO.getStatus().equals(SystemConstant.BOTH)) {
        builder.and(Q.status.eq(courseDTO.getStatus()));
      }
    }
    else {
      builder.and(Q.status.eq(SystemConstant.ENABLE));
    }
    if (courseDTO == null) {
      return builder;
    }
    if (courseDTO.getCategoryId() != null) {
      List<Long> listId =
          courseCategoryService.getTreeCategoryByParentId(courseDTO.getCategoryId());
      builder.and(Q.coursecategory.id.in(listId).or(Q.coursecategory.id.eq(courseDTO.getCategoryId())));
    }
    if (StringUtils.isNotBlank(courseDTO.getName())) {
      builder.and(Q.name.containsIgnoreCase(courseDTO.getName()));
    }
    return builder;
  }






}
