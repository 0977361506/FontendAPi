package com.vnpost.elearning.repository;

import com.vnpost.elearning.dto.MapDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageFunctionDao {

    public void sortByKeyAndValue( MapDTO mapDTO ,String key,String value){
           mapDTO.setSortKey(key);
           mapDTO.setSortValue(value);

    }

    public void equalByKeyAndValue( MapDTO mapDTO ,String key,Object value){
        if(mapDTO.getValuesEqual()==null){
            Map<String, Object> valuesEqual = new HashMap<>();
            mapDTO.setValuesEqual(valuesEqual);
        }
        mapDTO.getValuesEqual().put(key,value);
    }

    public void likeByKeyAndValue( MapDTO mapDTO ,String key,Object value){
        if(mapDTO.getValuesLike()==null){
            Map<String, Object> valuesLike = new HashMap<>();
            mapDTO.setValuesLike(valuesLike);
        }
        mapDTO.getValuesLike().put(key,value);
    }

    public void inByKeyAndValue( MapDTO mapDTO ,String key, List<Long> list){
        if(mapDTO.getValueIn()==null){
            Map<String, List<Long>> valueIn = new HashMap<>();
            mapDTO.setValueIn(valueIn);
        }
        mapDTO.getValueIn().put(key,list);
    }


    public void equalOrByKeyAndValue( MapDTO mapDTO ,  String[] key, Object[] list){
        if(mapDTO.getValuesLike()==null){
            Map<String[], Object[]> valuesEqualOr = new HashMap<>();
            mapDTO.setValuesEqualOr(valuesEqualOr);
        }
        mapDTO.getValuesEqualOr().put(key,list);
    }

}
