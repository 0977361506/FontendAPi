package com.vnpost.elearning.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class UsersDTO extends AbstractDTO<UsersDTO> {


    private String wardId;
    private Long courseId;
    private String courseName;
    private String provinceId;
    private String districtId;
    private String search;
    private String key;
    private String poscodeId;
    private MultipartFile importExcel;
    private Date birthday;
    private String poscodeName;
    private String email;
    private  String imageUsers;

    private String fullName;

    private long idPosition;

//    @JsonIgnore
    private PoscodeVnpostDTO poscodeVnpost;

    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String phoneNumber;

    private String place;


    private int gender;

    private int status;

    private Date lastUpdate;

    private Date timeCreate;

    private MultipartFile image;
    private String birthDateFomatted;

    private String idunit;

    private Long idCommuneVnpost;
    private String oldPassword;
    private Boolean isChangePassword;
}
