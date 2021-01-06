package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.UsersDTO;
import eln.common.core.entities.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class UserConverter implements IDTO<UsersDTO>, IEntity<User> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UsersDTO convertToDTO(Object entity) {
        User user = (User) entity;
        UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
        if (usersDTO.getBirthday() != null) {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            usersDTO.setBirthDateFomatted(simpleDateFormat.format(usersDTO.getBirthday()));
        }

        return usersDTO;
    }

    @Override
    public User convertToEntity(Object dto) {
        UsersDTO usersDTO = (UsersDTO) dto;
        User user = modelMapper.map(usersDTO, User.class);

        return user;
    }
}
