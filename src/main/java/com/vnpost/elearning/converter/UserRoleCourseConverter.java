package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.UserRoleCourseDTO;
import eln.common.core.entities.user.UserRoleCourse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleCourseConverter implements IEntity<UserRoleCourse>, IDTO<UserRoleCourseDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserRoleCourseDTO convertToDTO(Object entity) {
        UserRoleCourse userRoleCourse = (UserRoleCourse) entity;
        UserRoleCourseDTO userRoleCourseDTO = modelMapper.map(userRoleCourse, UserRoleCourseDTO.class);
        return userRoleCourseDTO;
    }

    @Override
    public UserRoleCourse convertToEntity(Object dto) {
        UserRoleCourseDTO userRoleCourseDTO = (UserRoleCourseDTO) dto;
        UserRoleCourse userRoleCourse = modelMapper.map(userRoleCourseDTO, UserRoleCourse.class);


        return userRoleCourse;
    }
}
