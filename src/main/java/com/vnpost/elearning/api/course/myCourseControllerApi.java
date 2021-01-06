package com.vnpost.elearning.api.course;

import com.vnpost.elearning.Beans.Dates;
import com.vnpost.elearning.converter.CourseConverter;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CourseJoinService;
import com.vnpost.elearning.service.CourseService;
import eln.common.core.entities.course.Course;
import eln.common.core.repository.course.CourseJoinRepository;
import eln.common.core.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping("/api")
public class myCourseControllerApi {
    @Autowired
    private Dates d;
    @Autowired
    private  CourseJoinService courseJoinService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseConverter converter;
    @Autowired
    private CourseJoinRepository courseJoinRepository ;

    @Autowired private CourseRepository courseRepository;
    @RequestMapping("/user/mycourse/complated/{index}")
    public Object[] myCourseComplated(@PathVariable("index") Integer index){
        Pageable pageable = PageRequest.of(index ,6);
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Course> courses =courseService.getListCoureForchecked(user.getId(),pageable);
//        Integer sl = courseJoinRepository.countItemInCourseJoin(user.getId());
        Page<Course> page =courseRepository.getAllByFinished(user.getId(),pageable);
        List<Course> courses = page.getContent();
        Integer sl = courseRepository.countItemInCourseJoin(user.getId());
        Object[] result = { converter.convertList(courses) , sl};
        return result;
    }




    @RequestMapping("/user/mycourse/complated/{index}/{key}")
    public Object[] SearchmyCourseComplated(@PathVariable("index") Integer index ,@PathVariable("key") String key){
        Pageable pageable = PageRequest.of(index ,6);
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Course> page =courseRepository.getAllCourseBySearch(user.getId(),"%" + key.trim() + "%",pageable);
        List<Course> courses = page.getContent();
        Integer sl = courseRepository.countSearchItemInCourseJoin(user.getId(),"%" + key.trim() + "%");
        Object[] result = { converter.convertList(courses) , sl};
        return result;
    }




    @PostMapping("/user/mycourse/finnish")
    public  ResponseEntity<ServiceResult> getCourseFinish(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(new ServiceResult(courseService.getCourseFinish(courseDTO),"Thanh công","200"));
    }
    @PostMapping("/user/mycourse/current")
    public  ResponseEntity<ServiceResult> myCourseCurent(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(new ServiceResult(courseService.myCourseCurent(courseDTO),"Thanh công","200"));
    }



}
