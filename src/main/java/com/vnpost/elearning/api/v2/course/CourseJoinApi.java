package com.vnpost.elearning.api.v2.course;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.security.MyUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * August 04,2020
 */
@AllArgsConstructor

@RestController(value = "courseJoinApiV2")
@RequestMapping("/api/v2")
public class CourseJoinApi {
    private CourseJoinProcessor courseJoinProcessor;
    @GetMapping("/course-join/check/{id}")
    public ResponseEntity<ServiceResult> checkCurrentUserInCourse(@PathVariable Long id) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ServiceResult result = new ServiceResult();
        if (courseJoinProcessor.exitsUserAndCourse(myUser.getId(), id)) {
            result.setData(true);
            result.setMessage("true");
        } else {
            result.setData(false);
            result.setMessage("false");
        }
        result.setCode("200");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
