package com.vnpost.elearning.dto;

import eln.common.core.entities.config.SlideShow;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SlideShowDTO {

    private String code;

    private String urlImage;

    private List<String> urlImages;

    private List<Long> oldIds;

    private List<Long> newIds;

    private List<SlideShow> slideShows;

    private List<String> codes;
}
