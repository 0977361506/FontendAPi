package com.vnpost.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitVnpostDTO extends AbstractDTO<UnitVnpostDTO> {


    private String name;

    private String parentUnitCode;


    private String unitTypeCode;
    @JsonIgnore
    private CommuneVnpostDTO communeVnpost;



}
