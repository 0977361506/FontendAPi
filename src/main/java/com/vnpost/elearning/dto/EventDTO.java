package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class EventDTO extends BaseDTO{
    private Long id;

    private Integer highlinghtEvent;

    private String pcode;

    private String tomtat;
    private String content;
    private String title;
    private String image;

    private Date lastUpdate;

    private Date timeCreate;

    private Date timeEnd;

    private Date timeStart;


    private Long idCategory;

    private Integer status;

    private Integer type;


    private Integer totalItems = 0;
    private Integer maxPageItems = 12;
    private Integer firstItem = 0;
    private Integer page = 1;
    private String sortExpression;
    private String sortDirection;
    private Integer totalPages;
}
