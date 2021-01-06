package com.vnpost.elearning.converter;

public interface IEntity <T> {
    default T convertToEntity(Object dto) {
        return  null;
    }
}
