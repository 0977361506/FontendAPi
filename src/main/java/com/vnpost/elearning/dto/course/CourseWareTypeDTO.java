package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseWareTypeDTO extends AbstractDTO<CourseWareTypeDTO> {

    private String name;

    private String images;

    private String code;

    @JsonIgnore
    private List<CourseWareDTO> courseWares= new ArrayList<>();
}
