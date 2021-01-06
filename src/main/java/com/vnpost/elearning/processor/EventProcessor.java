package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.EventConverter;
import com.vnpost.elearning.dto.EventDTO;
import com.vnpost.elearning.service.EventService;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.events.QEventt;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>August 10,2020
 */
@Service
@AllArgsConstructor
public class EventProcessor {
  private EventService eventService;
  private EventConverter converter;
  private final QEventt Q = QEventt.eventt;

  public List<EventDTO> findALl(EventDTO eventDTO, Pageable pageable) {
    BooleanBuilder builder = commonBuilder(eventDTO);
    return eventService.findAll(builder, pageable).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public Long count(EventDTO eventDTO) {
    BooleanBuilder builder = commonBuilder(eventDTO);
    return eventService.count(builder);
  }

  private BooleanBuilder commonBuilder(EventDTO eventDTO) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.status.eq(SystemConstant.ENABLE));
    if (eventDTO == null) {
      return builder;
    }
    if (StringUtils.isNotBlank(eventDTO.getTitle())) {
      builder.and(Q.title.containsIgnoreCase(eventDTO.getTitle()));
    }
    if (eventDTO.getIdCategory() != null) {
      builder.and(Q.detailCategoryEvent.id.eq(eventDTO.getIdCategory()));
    }
    return builder;
  }
}
