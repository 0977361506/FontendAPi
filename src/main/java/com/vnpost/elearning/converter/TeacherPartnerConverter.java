package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.course.TeacherPartnerDTO;
import eln.common.core.entities.TeacherPartner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeacherPartnerConverter implements IDTO<TeacherPartnerDTO>, IEntity<TeacherPartner> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TeacherPartnerDTO convertToDTO(Object entity) {
        TeacherPartner teacherPartner = (TeacherPartner) entity;
        TeacherPartnerDTO dto = modelMapper.map(teacherPartner, TeacherPartnerDTO.class);
        return dto;
    }

    @Override
    public TeacherPartner convertToEntity(Object dto) {
        TeacherPartnerDTO teacherPartnerDTO = (TeacherPartnerDTO) dto;
        TeacherPartner entity = modelMapper.map(teacherPartnerDTO, TeacherPartner.class);
        return entity;
    }
}
