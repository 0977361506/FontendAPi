package com.vnpost.elearning.security;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * May 14,2020
 */
@Getter
@Setter
public class AuthToken {
    private String token;
    private String expiredTime;
    public AuthToken(String token, String expiredTime) {
        this.token = token;
        this.expiredTime = expiredTime;
    }
}
