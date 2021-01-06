package com.vnpost.elearning.api.chat;

import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
public class ChatMessageApi {

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/api/current_user")
    public List<String> getCurrentUser(HttpServletRequest request) {

        MyUser authentication = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> result = new ArrayList<>();
        result.add(authentication.getUsername());
        for (GrantedAuthority permission: authentication.getAuthorities()) {
            result.add(permission.getAuthority());
        }

        return result;
    }
}
