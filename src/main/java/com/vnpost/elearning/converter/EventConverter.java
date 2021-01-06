package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.EventDTO;
import eln.common.core.entities.events.Eventt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventConverter implements IDTO<EventDTO>, IEntity<Eventt> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EventDTO convertToDTO(Object entity) {
        Eventt eventt = (Eventt) entity;
        EventDTO eventDTO = modelMapper.map(eventt, EventDTO.class);
        return eventDTO;
    }

    @Override
    public Eventt convertToEntity(Object dto) {
        EventDTO eventDTO = (EventDTO) dto;
        Eventt eventt = modelMapper.map(eventDTO, Eventt.class);
        return eventt;
    }
}
