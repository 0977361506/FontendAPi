package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.CourseCategoryConverter;
import com.vnpost.elearning.converter.ProposeCourseConverter;
import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import com.vnpost.elearning.dto.course.ProposeCourseDTO;
import com.vnpost.elearning.security.MyUser;
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
import java.util.SortedMap;
import java.util.stream.Collectors;

@Service
public class ProposeCourseService  {
    private final QProposeCourse Q = QProposeCourse.proposeCourse;
    @Autowired private ProposeCourseConverter proposeCourseConverter;
    @Autowired private ProposeCourseRepository proposeCourseRepository;
    @Autowired private CourseCategoryRepository courseCategoryRepository;
    @Autowired private CourseCategoryConverter courseCategoryConverter;




    public List<ProposeCourseDTO> find(ProposeCourseDTO proposeCourseDTO, Integer page,Integer size) {
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(page - 1, size,sort);
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BooleanBuilder builder = commonBooleanBuilder(proposeCourseDTO);
        builder.and(Q.idUser.eq(myUser.getId()));
        List<ProposeCourseDTO> proposeCourseList =   proposeCourseRepository.findAll(builder, pageable).stream()
                .map(proposeCourseConverter::convertToDTO)
                .collect(Collectors.toList());
        return getProposeCourse(proposeCourseList);
    }

    public Boolean savePropose(ProposeCourseDTO proposeCourseDTO) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        proposeCourseDTO.setIdUser(myUser.getId());
        proposeCourseRepository.save(proposeCourseConverter.convertToEntity(proposeCourseDTO));
        return true;
    }

    public Boolean updatePropose(ProposeCourseDTO proposeCourseDTO) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        proposeCourseDTO.setIdUser(myUser.getId());
        proposeCourseRepository.save(proposeCourseConverter.convertToEntity(proposeCourseDTO));
        return true;
    }
    public Boolean deletePropose(ProposeCourseDTO proposeCourseDTO) {
        proposeCourseRepository.deleteById(proposeCourseDTO.getId());
        return true;
    }


    private List<ProposeCourseDTO> getProposeCourse(List<ProposeCourseDTO> proposeCourseDTOList) {
        for (ProposeCourseDTO proposeCourseDTO : proposeCourseDTOList){
            List<String> categoryNames = new ArrayList<>();

            CoursecategoryDTO coursecategoryDTO = courseCategoryConverter.convertToDTO(courseCategoryRepository
                        .findById(proposeCourseDTO.getIdCourseCategory()).get());
            proposeCourseDTO.setCoursecategoryDTO(coursecategoryDTO);
            proposeCourseDTO.setCategoryNames(getCategoryParents(proposeCourseDTO.getIdCourseCategory(),categoryNames));
        }
        return proposeCourseDTOList;
    }
    private List<String> getCategoryParents(Long idCategory,List<String> categoryNames){
        Coursecategory coursecategory = courseCategoryRepository.findById(idCategory).get();
        categoryNames.add(coursecategory.getName());
        if(coursecategory.getParentId()==null){
            return categoryNames;
        }
        return getCategoryParents(coursecategory.getParentId(),categoryNames);
    }


    public Long count(ProposeCourseDTO proposeCourseDTO) {
        MyUser myUser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BooleanBuilder builder = commonBooleanBuilder(proposeCourseDTO);
        builder.and(Q.idUser.eq(myUser.getId()));
        return  proposeCourseRepository.count(builder);
    }

    protected BooleanBuilder commonBooleanBuilder(ProposeCourseDTO proposeCourseDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        if (proposeCourseDTO.getTimeRegister()!= null) {
            builder.and(Q.timeRegister.month().eq(DateUtils.getMonth(proposeCourseDTO.getTimeRegister())))
                    .and(Q.timeRegister.year().eq(DateUtils.getYear(proposeCourseDTO.getTimeRegister())));
        }
        return builder;
    }

    public ProposeCourseDTO findByIdPropose(Long id) {
        ProposeCourseDTO proposeCourseDTO = proposeCourseConverter
                .convertToDTO(proposeCourseRepository.findById(id).get());
        CoursecategoryDTO coursecategoryDTO = courseCategoryConverter.convertToDTO(courseCategoryRepository
                .findById(proposeCourseDTO.getIdCourseCategory()).get());
        proposeCourseDTO.setCoursecategoryDTO(coursecategoryDTO);
        return proposeCourseDTO;
    }

}
