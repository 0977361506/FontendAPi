package com.vnpost.elearning.processor;

import com.vnpost.elearning.converter.UserConverter;
import com.vnpost.elearning.dto.UsersDTO;
import com.vnpost.elearning.service.CommuneVnpostService;
import com.vnpost.elearning.service.PoscodeVnpostService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.entities.unit.CommuneVnpost;
import eln.common.core.entities.user.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProcessor {
    private UserService userService;
    private UserConverter userConverter;
    private PasswordEncoder passwordEncoder;
    private PoscodeVnpostService poscodeVnpostService;
    private CommuneVnpostService communeVnpostService;



    public void updatePassword(UsersDTO usersDTO) throws Exception {
        if (usersDTO.getId().equals(null)) throw new Exception("Chưa có Id người dùng");
        if (StringUtils.isBlank(usersDTO.getPassword())) throw new Exception("Chưa có password");
        if (StringUtils.isBlank(usersDTO.getOldPassword())) throw new Exception("Chưa nhập mật khẩu cũ");
        User entity = userService.findById(usersDTO.getId()).get();
        if (!passwordEncoder.matches(usersDTO.getOldPassword(),entity.getPassword())) throw new Exception("Mật khẩu cũ chưa chính xác");
        entity.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        userService.save(entity);
    }


    public UsersDTO findById(Long idUser){
         return userConverter.convertToDTO(userService.findById(idUser).get());
    }
    public void create(UsersDTO usersDTO) {
        User entity = userConverter.convertToEntity(usersDTO);
        if (StringUtils.isNotBlank(usersDTO.getPoscodeId())) {
            entity.setPoscodeVnpost(poscodeVnpostService.findByIdEntity(usersDTO.getPoscodeId()));
        } else {
            entity.setPoscodeVnpost(poscodeVnpostService.findByIdEntity(String.valueOf(1)));
        }
        Long idCommuneVnpost = usersDTO.getIdCommuneVnpost();
        if (idCommuneVnpost != null) {
            CommuneVnpost communeVnpost = communeVnpostService.findById(idCommuneVnpost);
            entity.setCommuneVnpost(communeVnpost);
        } else {
            entity.setCommuneVnpost(null);
        }
        userService.save(entity);
    }
}
