package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPermissionDTO {

    private long idUsers[];

    private long idPermistion;

    private String codenamePermistion;
}
