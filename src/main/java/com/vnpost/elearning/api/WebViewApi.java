package com.vnpost.elearning.api;


import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.ChapterService;
import com.vnpost.elearning.service.OutlineService;
import com.vnpost.elearning.service.SlideShowService;
import eln.common.core.entities.config.SlideShow;
import eln.common.core.entities.course.Outline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/web-view")
public class WebViewApi {
    @Autowired
    public OutlineService outlineService;
    @Autowired
    public CourseJoinProcessor courseJoinProcessor;
    @Autowired
    public ChapterService chapterService;


    @GetMapping("/permission")
    public ResponseEntity<Boolean> checkPermission(
            @RequestParam(required = false,name = "idChapter")  Long idChapter,
            @RequestParam(required = false,name = "idCourseWare")  Long idCourseWare) {
        try {
            MyUser myUser = (MyUser) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
            return ResponseEntity.ok(courseJoinProcessor.exitsUserAndCourse(myUser.getId()
                    ,outlineService.getCourseIdByid(chapterService.getIdOutlineById(idChapter))));
        }catch (Exception e){
            return  ResponseEntity.ok(false);
        }
    }
}
