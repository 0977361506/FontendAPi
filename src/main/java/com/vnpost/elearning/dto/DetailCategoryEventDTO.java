package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class DetailCategoryEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;

    private Date lastUpdate;

    private String nameDetail;

    private Date timeCreate;

    private Long parent;

    private String description;
}
