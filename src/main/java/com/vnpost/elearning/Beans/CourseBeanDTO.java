package com.vnpost.elearning.Beans;

import com.vnpost.elearning.dto.course.OutlineDTO;
import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CourseBeanDTO {
    private Long id;
    private Date createdDate;


    private String createdBy;


    private String dateTimeEnd;

    private String dateTimeStart;
    private String searchValue;

    private Date timeCreate;

    private Long competitionId;

    private String avatar;
    private PoscodeVnpostDTO poscodeVnpost;
    private String description;

    private Integer highlight;

    private String name;

    private String code;

    private Long price;

    private OutlineDTO outline;
    private Integer status;

    private Integer stepbystep;
}
