package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentCategoryDTO {
    private Long id;

    private String describes;

    //   private Date lastUpdate;

    private String nameDocument;

    private Long documentCategoryId;
    private Long parent;
}
