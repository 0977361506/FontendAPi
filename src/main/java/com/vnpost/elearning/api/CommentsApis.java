package com.vnpost.elearning.api;

import com.vnpost.elearning.dto.course.AnswerCommentDto;
import com.vnpost.elearning.dto.ClassCouresDTO;
import com.vnpost.elearning.dto.CommentDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.AnswerCommentService;
import com.vnpost.elearning.service.CommentService;
import com.vnpost.elearning.service.CourseService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.entities.course.AnswerComment;
import eln.common.core.entities.course.Comment;
import eln.common.core.entities.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class CommentsApis {
  @Autowired CommentDTO cmdto;
  @Autowired AnswerCommentService ans;
  @Autowired ClassCouresDTO dto;
  @Autowired CourseService courseService;
  @Autowired CommentService c;
  @Autowired
  UserService userService;
  @Autowired
  AnswerCommentDto answerCommentDto;
  @GetMapping("/api/getCommentForCourse/{idc}/{pageNo}")
  public List<CommentDTO> getListCommentForCourse(@PathVariable("idc") Long idc, @PathVariable("pageNo") int page){
      List<Comment> comments = c.getCommentByPageNo(page,6,idc);
      List<CommentDTO> commentDTOS = cmdto.convertToList(comments);
      return  commentDTOS;
  }
  @GetMapping("/api/comment/{a}/{b}")
  public ResponseEntity<CommentDTO> getComment(
      @PathVariable("a") String comment, @PathVariable("b") Long b) {
    if (!comment.equals("")) {
      MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      Course cc = courseService.findById(b).get();// tim kiếm khoa học trong list khóa học
      Comment comment2 = new Comment();
      comment2.setContents(comment);
      comment2.setCourse(cc);
      comment2.setCreatedDate(new Date());
      comment2.setUser(userService.findByUsername(user.getUsername()));
      c.save(comment2);
      return new ResponseEntity<CommentDTO>(cmdto.convertFromComment(comment2), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }



  @GetMapping("/api/recomment/{b}")
  public ResponseEntity<List<AnswerCommentDto>> getReComment(@PathVariable("b") Long b) {
    Comment comment2 = c.findById(b).get();
    CommentDTO commentDTO = cmdto.convertFromComment(comment2);
    List<AnswerComment> list = commentDTO.getList();

    return new ResponseEntity<List<AnswerCommentDto>>(answerCommentDto.convertToListDto(list), HttpStatus.OK);
  }

  @GetMapping("/api/recomments/{a}/{b}")
  public ResponseEntity<AnswerCommentDto> getReComment(
      @PathVariable("a") String comment, @PathVariable("b") Long b) {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (!comment.equals("")) {
      Comment commentss = c.findById(b).get();

      AnswerComment comment2 = new AnswerComment();
      comment2.setContents(comment);
      comment2.setCreatedBy(user.getFullName());
      comment2.setComment(commentss);
      comment2.setCreatedDate(new Date());
      comment2.setUser(userService.findByUsername(user.getUsername()));
      ans.save(comment2);

      return new ResponseEntity<AnswerCommentDto>(answerCommentDto.convertToDto(comment2), HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
