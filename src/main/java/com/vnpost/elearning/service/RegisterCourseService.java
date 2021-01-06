package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.CourseConverter;
import com.vnpost.elearning.converter.ProposeCourseConverter;
import com.vnpost.elearning.converter.RegisterCourseConverter;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import com.vnpost.elearning.dto.course.ProposeCourseDTO;
import com.vnpost.elearning.dto.course.RegisterCourseDTO;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.chapter.ChapterCourseWare;
import eln.common.core.entities.course.*;
import eln.common.core.repository.course.*;
import eln.common.core.uitils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class RegisterCourseService  extends CommonRepository<RegisterCourse, RegisterCourseRepository>  {
    public RegisterCourseService(RegisterCourseRepository repo) {
        super(repo);
    }
    private final QRegisterCourse Q = QRegisterCourse.registerCourse;
    private final QCourse Qc = QCourse.course;
    @Autowired private RegisterCourseConverter registerCourseConverter;
    @Autowired private CourseConverter courseConverter;
    @Autowired private CourseRepository courseRepository;
    @Autowired private RegisterCourseRepository registerCourseRepository;
    @Autowired private CourseCategoryRepository courseCategoryRepository;




    public List<RegisterCourseDTO> find(CourseDTO courseDTO, Integer page,Integer size) {
        Sort sort = Sort.by(Sort.Order.desc("modifiedDate"));
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BooleanBuilder builder = commonBooleanBuilder(courseDTO);
        List<CourseDTO> listCourse =   courseRepository.findAll(builder, pageable).stream()
                .map(courseConverter::convertToDTO)
                .collect(Collectors.toList());
        return getProposeCourse(listCourse,myUser);
    }

    public Boolean savePropose(RegisterCourseDTO registerCourseDTO) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        registerCourseDTO.setIdUser(myUser.getId());
        registerCourseRepository.save(registerCourseConverter.convertToEntity(registerCourseDTO));
        return true;
    }

    public Boolean updatePropose(RegisterCourseDTO registerCourseDTO) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        registerCourseDTO.setIdUser(myUser.getId());
        registerCourseRepository.save(registerCourseConverter.convertToEntity(registerCourseDTO));
        return true;
    }
    public Boolean deletePropose(RegisterCourseDTO registerCourseDTO) {
        registerCourseRepository.deleteById(registerCourseDTO.getId());
        return true;
    }


    private List<RegisterCourseDTO> getProposeCourse(List<CourseDTO> listCourse, MyUser myUser) {
        List<RegisterCourseDTO> registerCourseDTOS = new ArrayList<>();
        for (CourseDTO courseDTO : listCourse){
            RegisterCourseDTO registerCourseDTO = new RegisterCourseDTO();
            List<String> categoryNames = new ArrayList<>();
            RegisterCourse registerCourse  = registerCourseRepository.findByIdCourseAndIdUser(courseDTO.getId(),myUser.getId());
            if(registerCourse!=null){
                registerCourseDTO  = registerCourseConverter.convertToDTO(registerCourseRepository
                        .findByIdCourseAndIdUser(courseDTO.getId(),myUser.getId()));
            }
            registerCourseDTO
                    .setCategoryNames(getCategoryParents(courseDTO.getCoursecategory().getId(),categoryNames));
            registerCourseDTO.setCourseDTO(courseDTO);
            registerCourseDTOS.add(registerCourseDTO);
        }
        return registerCourseDTOS;
    }

    private List<String> getCategoryParents(Long idCategory,List<String> categoryNames){
        Coursecategory coursecategory = courseCategoryRepository.findById(idCategory).get();
        categoryNames.add(coursecategory.getName());
        if(coursecategory.getParentId()==null){
            return categoryNames;
        }
        return getCategoryParents(coursecategory.getParentId(),categoryNames);
    }

    public Long count(CourseDTO courseDTO) {
        BooleanBuilder builder = commonBooleanBuilder(courseDTO);
        return  courseRepository.count(builder);
    }

    protected BooleanBuilder commonBooleanBuilder(CourseDTO courseDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        if (courseDTO.getCreatedDate()!= null) {
            builder.and(Qc.courseConfig.startLearning.year().eq(DateUtils.getYear(courseDTO.getCreatedDate())));
        }
        return builder;
    }
}
