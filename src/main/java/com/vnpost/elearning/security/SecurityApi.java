package com.vnpost.elearning.security;

import com.vnpost.elearning.api.course.CourseRequestApi;
import eln.common.core.entities.user.Permistion;
import eln.common.core.entities.user.User;
import eln.common.core.entities.user.UserPermistion;
import eln.common.core.repository.user.UserRepository;
import eln.common.core.security.LoginAttemptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/login")
public class SecurityApi {
    private final Logger logger = LogManager.getLogger(SecurityApi.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired private LoginAttemptService loginAttemptService;

    @PostMapping("/user")
    public UserModel getUserByUserName(@RequestBody UserModel userModel) {
        User userEntity = userRepository.findByUsernameAndStatus(userModel.getUsername(),userModel.getStatus());
        UserModel result = new UserModel();
        if (loginAttemptService.isBlocked(userModel.getUsername())) {
            logger.warn("user blocked");
            throw new RuntimeException("Block");
        }
        if (userEntity != null) {
            result.setCode("200");
            result.setUsername(userEntity.getUsername());
            result.setFullName(userEntity.getFullName());
            result.setId(String.valueOf(userEntity.getId()));
            result.setPassword(userEntity.getPassword());

            result.setPosCode((userEntity.getPoscodeVnpost()==null)?null:userEntity.getPoscodeVnpost().getId());
            result.setUnitCode((userEntity.getPoscodeVnpost()==null)?null:userEntity.getPoscodeVnpost().getId());
            if(userEntity.getPoscodeVnpost()!=null){
                result.setPosCode(userEntity.getPoscodeVnpost().getId());
            }
            List<String> permission = new ArrayList<>();
            for (UserPermistion userPermistion : userEntity.getUserPermistions()) {
                Permistion permistion = userPermistion.getPermistion();
                permission.add(permistion.getCodename());
            }
            result.setEmail(userEntity.getEmail());
            result.setPermissionNames(permission);
            result.setBirthday(userEntity.getBirthday());
            result.setGender(userEntity.getGender());
            result.setPhoneNumber(userEntity.getPhoneNumber());
            result.setPlace(userEntity.getPlace());
            result.setIsChangePassword(userEntity.getIsChangePassword());
            loginAttemptService.loginSucceeded(userModel.getUsername());
        }
        else {
            loginAttemptService.loginFailed(userModel.getUsername());
            result.setCode("500");
        }
        return result;
    }


}
