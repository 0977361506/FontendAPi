package com.vnpost.elearning.api.v2.user;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.UsersDTO;
import com.vnpost.elearning.processor.UserProcessor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/user")
@RestController("userV2")
@AllArgsConstructor
public class UserApi {
    private UserProcessor userProcessor;
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<ServiceResult> createUser(@RequestBody UsersDTO usersDTO) {
        String pass = usersDTO.getPassword();
        usersDTO.setPassword(passwordEncoder.encode(pass));
        usersDTO.setStatus(1);
        userProcessor.create(usersDTO);
        return ResponseEntity.ok(new ServiceResult("success","200"));
    }
}
