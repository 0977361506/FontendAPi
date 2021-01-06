package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistrictVnpostDTO extends AbstractDTO<DistrictVnpostDTO> {

    private String name;

    private String description;

    private ProvinceVnpostDTO provinceVnpost;


//    private List<CommuneVnpostDTO> communeVnposts = new ArrayList<>();
}
