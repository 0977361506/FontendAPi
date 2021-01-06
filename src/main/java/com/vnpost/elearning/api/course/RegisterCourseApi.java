package com.vnpost.elearning.api.course;


import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.RegisterCourseDTO;
import com.vnpost.elearning.processor.RegisterCourseProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register-course")
public class RegisterCourseApi {
    @Autowired
    private RegisterCourseProcessor processor;


    @PostMapping("/list")
    public ResponseEntity<ServiceResult> getList(@RequestParam(required = false, defaultValue = "14") Integer size,
                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestBody CourseDTO courseDTO) {
        return new ResponseEntity<>(processor.getList(courseDTO,page,size), HttpStatus.OK);
    }
    @PostMapping("/registration-course")
    public  ResponseEntity<ServiceResult> save(@RequestBody RegisterCourseDTO registerCourseDTO) {
        return new ResponseEntity<>(processor.save(registerCourseDTO), HttpStatus.OK);
    }
    @PutMapping("/registration-course")
    public  ResponseEntity<ServiceResult> update(@RequestBody RegisterCourseDTO registerCourseDTO) {
        return new ResponseEntity<>(processor.update(registerCourseDTO), HttpStatus.OK);
    }
    @DeleteMapping("/registration-course")
    public  ResponseEntity<ServiceResult> delete(@RequestBody RegisterCourseDTO registerCourseDTO) {
        return new ResponseEntity<>(processor.delete(registerCourseDTO), HttpStatus.OK);
    }



}
