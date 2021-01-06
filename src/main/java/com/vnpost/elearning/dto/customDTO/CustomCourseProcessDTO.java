package com.vnpost.elearning.dto.customDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomCourseProcessDTO implements Serializable {
   private Long id;
   private String namecourseWare;
   private String namecourseWareType;
   private Integer status;


}
