package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingModelDTO <T> {
    private List<T> listResult;
   // private String tableId = "tableList";
    private Integer totalItems = 0;
    private Integer maxPageItems = 12;
    private Integer firstItem = 0;
    private Integer page = 1;
    private String sortExpression;
    private String sortDirection;
    private Integer totalPages;
}
