package com.vnpost.elearning.security;

import eln.common.core.entities.user.User;
import eln.common.core.entities.user.UserPermistion;
import eln.common.core.repository.user.UserRepository;
import eln.common.core.security.LoginAttemptService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * May 14,2020
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired private LoginAttemptService loginAttemptService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User userEntity = userRepository.findByUsernameAndStatus(username,1);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User.java not found");
        }
        if (loginAttemptService.isBlocked(username)) {
            logger.warn("user blocked");
            throw new RuntimeException("Block");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserPermistion item: userEntity.getUserPermistions()) {
            authorities.add(new SimpleGrantedAuthority(item.getPermistion().getCodename()));
        }
        MyUser myUserDetail = new MyUser(userEntity.getUsername(), userEntity.getPassword()
                ,true,true,true,true, authorities);
        BeanUtils.copyProperties(userEntity, myUserDetail);
        if(userEntity.getPoscodeVnpost()!=null){
            myUserDetail.setPosCode(userEntity.getPoscodeVnpost().getId());
        }
        myUserDetail.setFullName(userEntity.getFullName());
        myUserDetail.setId(userEntity.getId());
        myUserDetail.setEmail(userEntity.getEmail());
        return myUserDetail;
    }
}
