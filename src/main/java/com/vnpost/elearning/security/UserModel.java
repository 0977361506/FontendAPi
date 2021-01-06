package com.vnpost.elearning.security;

import eln.common.core.entities.unit.PoscodeVnpost;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Getter
@Setter

public class UserModel {
    private String fullName;
    private String id;
    private String posCode;
    private String unitCode;
    private String code;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private List<String> permissionNames;
    private Integer status;
    private List<String> roles;
    private String email;
    private PoscodeVnpost poscodeVnpost;
    private Date birthday;

    private String phoneNumber;

    private String place;

    private Integer gender;
    private Boolean isChangePassword;

}
