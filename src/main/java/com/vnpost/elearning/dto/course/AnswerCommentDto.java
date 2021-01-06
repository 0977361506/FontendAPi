package com.vnpost.elearning.dto.course;

import eln.common.core.entities.course.AnswerComment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Component
public class AnswerCommentDto {
    private Long id ;
    private String contents;
    private String createdBy;
    private  String avatar ;
    private Date dateCommnet ;
    private Long idComment;
    public AnswerCommentDto convertToDto(AnswerComment answerComment){
        AnswerCommentDto answerCommentDto = new AnswerCommentDto();
        answerCommentDto.setCreatedBy(answerComment.getCreatedBy());
        answerCommentDto.setContents(answerComment.getContents());
        answerCommentDto.setAvatar(answerComment.getUser().getImageUsers());
        answerCommentDto.setDateCommnet(answerComment.getCreatedDate());
        return  answerCommentDto;
    }
    public List<AnswerCommentDto> convertToListDto(List<AnswerComment> answerComments){
        List<AnswerCommentDto> commentDtos = new ArrayList<>();
        for(AnswerComment a: answerComments){
            commentDtos.add(convertToDto(a));
        }
        return  commentDtos;
    }
}
