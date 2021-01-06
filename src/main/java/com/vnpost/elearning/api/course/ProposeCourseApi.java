package com.vnpost.elearning.api.course;


import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.course.ProposeCourseDTO;
import com.vnpost.elearning.processor.ProposeCourseProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propose-course")
public class ProposeCourseApi {

    @Autowired
    private ProposeCourseProcessor processor;

    @PostMapping("/list")
    public ResponseEntity<ServiceResult> getList(@RequestParam(required = false, defaultValue = "14") Integer size,
                                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestBody ProposeCourseDTO proposeCourseDTO) {
        return new ResponseEntity<>(processor.getListProposeCourse(proposeCourseDTO,page,size), HttpStatus.OK);
    }
    @PostMapping("/edit")
    public  ResponseEntity<ServiceResult> save(@RequestBody ProposeCourseDTO proposeCourseDTO) {
        return new ResponseEntity<>(processor.save(proposeCourseDTO), HttpStatus.OK);
    }
    @PutMapping("/edit")
    public  ResponseEntity<ServiceResult> update(@RequestBody ProposeCourseDTO proposeCourseDTO) {
        return new ResponseEntity<>(processor.update(proposeCourseDTO), HttpStatus.OK);
    }
    @DeleteMapping("/edit")
    public  ResponseEntity<ServiceResult> delete(@RequestBody ProposeCourseDTO proposeCourseDTO) {
        return new ResponseEntity<>(processor.delete(proposeCourseDTO), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ServiceResult> findById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(processor.findById(id), HttpStatus.OK);
    }

}
