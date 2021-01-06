package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



@Getter
@Setter
public class MailConfigDTO {


    private Long id;


    private String code;

    private String name;

    private Integer timeBeforeStart;

    private String subjects;

    private String content;


}
