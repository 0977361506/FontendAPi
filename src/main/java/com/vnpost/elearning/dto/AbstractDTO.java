package com.vnpost.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class AbstractDTO<T> implements Serializable {

    private Long id;
    private String searchValue;
    private String[] listArray;
    private String urlMapping;
    private String message;
    @JsonIgnore
    private Long[] ids = new Long[]{};
    private Integer totalItems = 0;
    private Integer maxPageItems = 12;
    private Integer firstItem = 0;
    private Integer page = 1;
    private String sortExpression;
    private String sortDirection;
    private Integer totalPages;


}
