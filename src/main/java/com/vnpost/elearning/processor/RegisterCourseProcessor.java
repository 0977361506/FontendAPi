package com.vnpost.elearning.processor;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.RegisterCourseDTO;
import com.vnpost.elearning.service.RegisterCourseService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class RegisterCourseProcessor {
    private  RegisterCourseService registerCourse;
    private final Logger logger = LogManager.getLogger(ProposeCourseProcessor.class);


    public ServiceResult getList(CourseDTO courseDTO, Integer page, Integer size) {
        try {
            Long totalItem = registerCourse.count(courseDTO);
            List<RegisterCourseDTO> listCourse = registerCourse.find(courseDTO, page,size);
            return  new ServiceResult(listCourse, totalItem,page, "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }


    public ServiceResult save(RegisterCourseDTO registerCourseDTO) {
        try {
            return  new ServiceResult(registerCourse.savePropose(registerCourseDTO), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }
    public ServiceResult update(RegisterCourseDTO registerCourseDTO) {
        try {
            return  new ServiceResult(registerCourse.updatePropose(registerCourseDTO), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }

    public ServiceResult delete(RegisterCourseDTO registerCourseDTO) {
        try {
            return  new ServiceResult(registerCourse.deletePropose(registerCourseDTO), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }

}
