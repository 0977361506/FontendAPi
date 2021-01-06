package com.vnpost.elearning.processor;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.course.ProposeCourseDTO;
import com.vnpost.elearning.service.ProposeCourseService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ProposeCourseProcessor {
    private  ProposeCourseService proposeCourseService;

    private final Logger logger = LogManager.getLogger(ProposeCourseProcessor.class);
    public ServiceResult getListProposeCourse( ProposeCourseDTO proposeCourseDTO, Integer page, Integer size) {
        try {
            Long totalItem = proposeCourseService.count(proposeCourseDTO);
            List<ProposeCourseDTO> listCourse = proposeCourseService.find(proposeCourseDTO, page,size);
            return  new ServiceResult(listCourse, totalItem,page, "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }

    public ServiceResult save(ProposeCourseDTO proposeCourseDTO) {
        try {
            return  new ServiceResult(proposeCourseService.savePropose(proposeCourseDTO), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }
    public ServiceResult update(ProposeCourseDTO proposeCourseDTO) {
        try {
            return  new ServiceResult(proposeCourseService.updatePropose(proposeCourseDTO), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }

    public ServiceResult delete(ProposeCourseDTO proposeCourseDTO) {
        try {
            return  new ServiceResult(proposeCourseService.deletePropose(proposeCourseDTO), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }

    public ServiceResult findById(Long id) {
        try {
            return  new ServiceResult(proposeCourseService.findByIdPropose(id), "success","200");
        }catch (Exception e){
            logger.error(e.getMessage());
            return  new ServiceResult(null, "error","500");
        }
    }




}
