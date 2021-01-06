package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author:Nguyen Anh Tuan
 * <p>
 * June 10,2020
 */
@Getter
@Setter
public class BaseDTO {
    private Long id;
    private String createdBy;
    private String modifiedBy;
    private Date createdDate;
    private Date modifiedDate;
    private Date timeCreate;
}
