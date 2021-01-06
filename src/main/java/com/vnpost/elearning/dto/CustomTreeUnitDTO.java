package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomTreeUnitDTO  extends AbstractDTO<CustomTreeUnitDTO> {
    List<UnitVnpostDTO> listUnitVnpostDTOs;
    List<PoscodeVnpostDTO> lisPoscodeVnposts;

}
