package com.vnpost.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoscodeVnpostDTO  extends AbstractDTO<PoscodeVnpostDTO> {


    private String name;


    private String address;


    private String typeCode;


    private String levelCode;

    private String isOffice;


    private String status;

    @JsonIgnore
    private ProvinceVnpostDTO provinceVnpost;

    @JsonIgnore
    private CommuneVnpostDTO communeVnpost;

    private UnitVnpostDTO unitVnpost;


}
