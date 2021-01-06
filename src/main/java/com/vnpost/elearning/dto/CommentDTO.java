package com.vnpost.elearning.dto;

import eln.common.core.entities.course.AnswerComment;
import eln.common.core.entities.course.Comment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Getter
@Setter
public class CommentDTO {

  private Long id;
  @NotBlank private String contents;
  @NotNull private Long courseId;

  private String createdBy;

  private Date createdDate;

  private String modifiedBy;

  private Date modifiedDate;

  private String urlImage;

  private List<AnswerComment> list;
  // bi-directional many-to-one association to User.java
  public CommentDTO convertFromComment(Comment comment) {
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setContents(comment.getContents());
    commentDTO.setId(comment.getId());
    commentDTO.setCreatedBy(comment.getCreatedBy());
    commentDTO.setCreatedDate(comment.getCreatedDate());
    commentDTO.setList(comment.getList());
    if (comment.getUser() != null) {
      commentDTO.setUrlImage(comment.getUser().getImageUsers());
    }
    return commentDTO;
  }

  public List<CommentDTO> convertToList(List<Comment> list) {
    List<CommentDTO> commentDTOS = new ArrayList<>();
    for (Comment c : list) {
      commentDTOS.add(convertFromComment(c));
    }
    return commentDTOS;
  }
}
