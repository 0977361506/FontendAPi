package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.CommentConverter;
import com.vnpost.elearning.dto.course.AnswerCommentDto;
import com.vnpost.elearning.dto.CommentDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.*;
import eln.common.core.entities.user.User;
import eln.common.core.exception.CommentException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 04,2020
 */
@Service
@AllArgsConstructor
public class CommentProcessor {
  private final Logger logger = LogManager.getLogger(CommentProcessor.class);
  private final QComment Q = QComment.comment;
  private final QAnswerComment qAnswerComment = QAnswerComment.answerComment;
  private CommentService commentService;
  private CommentConverter converter;
  private UserService userService;
  private CourseService courseService;
  private CourseJoinService courseJoinService;
  private AnswerCommentService answerCommentService;

  public void create(CommentDTO commentDTO) throws CommentException {
    validComment(commentDTO.getCourseId());
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Optional<Course> course = courseService.findById(commentDTO.getCourseId());
    User user = userService.findByUsername(myUser.getUsername());
    Comment comment = converter.convertToEntity(commentDTO);
    comment.setUser(user);
    comment.setCourse(course.get());
    commentService.save(comment);
  }

  public List<CommentDTO> findByCourseId(Long courseId) throws CommentException {
    validComment(courseId);
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.course.id.eq(courseId));
    return commentService.findAll(builder, PageRequest.of(0, 99999999, Sort.by("id").descending()))
        .stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<AnswerComment> findAllByCommentId(Long commentId) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(qAnswerComment.comment.id.eq(commentId));
    return answerCommentService.findAll(
        builder, PageRequest.of(0, 9999999, Sort.by("id").descending()));
  }

  public void createRecomment(AnswerCommentDto answerCommentDto) {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    AnswerComment answerComment = new AnswerComment();
    answerComment.setContents(answerCommentDto.getContents());
    answerComment.setComment(commentService.findById(answerCommentDto.getIdComment()).get());
    answerComment.setUser(userService.findByUsername(myUser.getUsername()));
    answerCommentService.save(answerComment);
  }

  private void validComment(Long courseId) throws CommentException {
    MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (myUser == null) {
      logger.info("Tài khoản chưa đăng nhập");
      throw new CommentException("Yêu cầu đăng nhập");
    }
    Optional<Course> course = courseService.findById(courseId);
    if (!course.isPresent()) {
      throw new CommentException("Không tìm thấy khóa học");
    }
    if (!courseJoinService.existUserInCourse(myUser.getId(), courseId)) {
      logger.info("User.java not in course");
      throw new CommentException("Học viên chưa có trong khóa học");
    }
    if (course.get().getStatus() == SystemConstant.DISABLE) {
      throw new CommentException("Khóa học đang bị khóa");
    }
  }
}
