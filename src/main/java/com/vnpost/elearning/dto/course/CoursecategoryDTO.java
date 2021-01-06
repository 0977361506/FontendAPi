package com.vnpost.elearning.dto.course;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoursecategoryDTO extends AbstractDTO<CoursecategoryDTO> {

    private String name;

    private String description;

    private Long parentId;

    private Integer isActive;

    private String nameParent;
}
