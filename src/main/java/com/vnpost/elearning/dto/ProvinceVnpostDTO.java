package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinceVnpostDTO extends AbstractDTO<ProvinceVnpostDTO> {


    private String name;

    private String description;


//    List<DistrictVnpostDTO> districtVnposts = new ArrayList<>();
}
