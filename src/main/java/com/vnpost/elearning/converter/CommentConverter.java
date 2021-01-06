package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.CommentDTO;
import eln.common.core.entities.course.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter implements IDTO<CommentDTO>, IEntity<Comment> {
  @Autowired private ModelMapper modelMapper;

  @Override
  public CommentDTO convertToDTO(Object entity) {
    Comment comment = (Comment) entity;
    CommentDTO dto = modelMapper.map(entity, CommentDTO.class);
    if (comment.getUser() != null) {
      dto.setUrlImage(comment.getUser().getImageUsers());
    }

    return dto;
  }

  @Override
  public Comment convertToEntity(Object dto) {
    Comment entity = modelMapper.map(dto, Comment.class);
    return entity;
  }
}
