package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommuneVnpostDTO extends AbstractDTO<CommuneVnpostDTO> {


    private String name;

    private DistrictVnpostDTO districtVnpost;

//    private List<UnitVnpostDTO> unitVnposts = new ArrayList<>();
//
//
//    private List<PoscodeVnpostDTO> poscodeVnposts = new ArrayList<>();
}
