package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocumentDTO extends BaseDTO {

    private Long courseId;
    private String describes;

    private String id_unit;

    private Date lastUpdate;

    private String linkFile;

    private String name;

    private Integer shares;
    private DocumentCategoryDTO documentCategoryDTO;
    private Integer status;
    private String originName;
    private Long id_poscode;
    private String poscodeName;
    private String sizes;
    private String type;

    private Long id_limit;
    private Long id_prioritize;
    private Integer allowedDownload;
    private Long idCategory;

    private Integer totalItems = 0;
    private Integer maxPageItems = 12;
    private Integer firstItem = 0;
    private Integer page = 1;
    private Integer totalPages;
}
