package com.vnpost.elearning.dto.course;

import com.vnpost.elearning.dto.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseDTO extends BaseDTO {
  private Long competitionId;
  private Long categoryId;
  private String categoryName;
  private String poscodeName;
  private String pcode;
  private String unitCode;
  private String avatar;

  private String description;

  private Integer highlight;

  private Integer isOnline;

  private String name;

  private String code;

  private Long price;

  private Integer showName;

  private Integer status;

  private Integer stepbystep;

  private PoscodeVnpostDTO poscodeVnpost;
  private Integer percentFinish;

  private CourseConfigDTO courseConfig;
  private CoursecategoryDTO coursecategory;

  private List<RateDTO> rates = new ArrayList<>();

  private OutlineDTO outline;

  private Integer totalItems = 0;
  private Integer maxPageItems = 12;
  private Integer firstItem = 0;
  private Integer page = 1;
  private Boolean isUserRated;
  @NotNull
  private String typeMyCourse;

  private Integer isHasCertificate;
}
