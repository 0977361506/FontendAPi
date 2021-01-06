package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDTO extends BaseDTO {

  private String tomtat;

  private String images;

  private String content;

  private String title;

  private Long id_detail_category;

  private Integer highlightNew;

  private Integer statusNew;

  private String pcode;

  private Integer type;

  private Long categoryId;

  private Integer totalItems = 0;
  private Integer maxPageItems = 12;
  private Integer firstItem = 0;
  private Integer page = 1;
  private String sortExpression;
  private String sortDirection;
  private Integer totalPages;


}
