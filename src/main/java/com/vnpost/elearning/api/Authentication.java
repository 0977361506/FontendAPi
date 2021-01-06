package com.vnpost.elearning.api;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.security.*;
import eln.common.core.repository.user.UserRepository;
import eln.common.core.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * May 16,2020
 */
@RestController
public class Authentication {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired private LoginAttemptService loginAttemptService;
    @Autowired
    private TokenProvider jwtTokenUtil;

    @PostMapping("/api/authentication")
    public ResponseEntity<ServiceResult> register(@Valid @RequestBody UserModel userDTO) throws AuthenticationException {
        if(loginAttemptService.isBlocked(userDTO.getUsername())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            final org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),
                            userDTO.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenUtil.generateToken(authentication);
            ServiceResult serviceResult = new ServiceResult();
            LocalDateTime expiredTime = LocalDateTime.now().plusDays(1);
            serviceResult.setData(new AuthToken(token,expiredTime.toString()));
            serviceResult.setCode("200");

            return new ResponseEntity<>(serviceResult, HttpStatus.OK);
        }catch (Exception e){

            throw  new UsernameNotFoundException("incorrect password or username");
        }

    }
}
