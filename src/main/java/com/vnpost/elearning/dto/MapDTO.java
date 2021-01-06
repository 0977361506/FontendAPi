package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MapDTO {
    private Map<String,Object> valuesLike;
    private Map<String,Object> valuesEqual;
    private Map<String[],Object[]> valuesEqualOr;
    private Map<String, List<Long>> valueIn ;
    private String  sortKey;
    private String  sortValue;
    public MapDTO() {
    }

    public MapDTO( Map<String, List<Long>>  valueIn, Map<String,
            Object> valuesEqual, Map<String, Object> valuesLike,Map<String[], Object[]> valuesEqualOr) {
        this.valueIn = valueIn;
        this.valuesEqual = valuesEqual;
        this.valuesLike = valuesLike;
        this.valuesEqualOr = valuesEqualOr;
    }
}
