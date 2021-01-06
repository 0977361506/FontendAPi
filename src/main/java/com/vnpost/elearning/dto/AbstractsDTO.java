package com.vnpost.elearning.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractsDTO {
    private Integer limit = 12;
    private Integer offset = 0;
    private MapDTO mapDTO;

    public AbstractsDTO(Integer limit, Integer offset , MapDTO mapDTO) {
        this.limit = limit;
        this.offset = offset;
        this.mapDTO = mapDTO;
    }
}
