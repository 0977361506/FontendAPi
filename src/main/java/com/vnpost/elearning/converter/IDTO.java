package com.vnpost.elearning.converter;

public interface IDTO <T> {
    default T convertToDTO(Object entity){
        return null ;
    }
}
