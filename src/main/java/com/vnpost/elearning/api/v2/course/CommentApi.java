package com.vnpost.elearning.api.v2.course;

import com.vnpost.elearning.dto.course.AnswerCommentDto;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CommentProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("CommentApiV2")
@RequestMapping("/api/v2")
@AllArgsConstructor
public class CommentApi {
    private CommentProcessor commentProcessor;

    @GetMapping("/recomment/{commentId}")
    public ResponseEntity<ServiceResult> getRecommentByCommentId(@PathVariable Long commentId) {
        return new ResponseEntity<>(new ServiceResult(commentProcessor.findAllByCommentId(commentId), "success", "200"), HttpStatus.OK);
    }

    @PostMapping("/recomment")
    public ResponseEntity<ServiceResult> insertRecomment(@RequestBody AnswerCommentDto answerCommentDto) {
        commentProcessor.createRecomment(answerCommentDto);
        return new ResponseEntity<>(new ServiceResult("Success","200"),HttpStatus.OK);
    }
}
