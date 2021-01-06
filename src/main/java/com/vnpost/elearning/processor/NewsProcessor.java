package com.vnpost.elearning.processor;

import com.querydsl.core.BooleanBuilder;
import com.vnpost.elearning.converter.NewsConverter;
import com.vnpost.elearning.dto.NewsDTO;
import com.vnpost.elearning.service.NewService;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.news.QNew;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 11,2020
 */
@Service
@AllArgsConstructor
public class NewsProcessor {
  private NewService newService;
  private NewsConverter converter;
  private final QNew Q = QNew.new$;

  public List<NewsDTO> findAll() {
    return newService.findByStatus(SystemConstant.ENABLE).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<NewsDTO> findAll(NewsDTO newsDTO, Pageable pageable) {
    BooleanBuilder builder = commonBuilder(newsDTO);
    return newService.findAll(builder, pageable).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }

  public Long count(NewsDTO newsDTO) {
    BooleanBuilder builder = commonBuilder(newsDTO);
    return newService.count(builder);
  }

  private BooleanBuilder commonBuilder(NewsDTO newsDTO) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.statusNew.eq(SystemConstant.ENABLE));
    if (newsDTO == null) {
      return builder;
    }
    if (StringUtils.isNotBlank(newsDTO.getTitle())) {
      builder.and(Q.title.containsIgnoreCase(newsDTO.getTitle()));
    }
    if (newsDTO.getCategoryId() != null) {
      builder.and(Q.newCategory.id.eq(newsDTO.getCategoryId()));
    }
    return builder;
  }

  public NewsDTO findById(Long id) {
    return converter.convertToDTO(newService.findByIdAndStatus(id, SystemConstant.ENABLE));
  }

  public List<NewsDTO> findByCategoryId(Long categoryId) {
    return newService.findByIdCategory(categoryId).stream()
        .map(converter::convertToDTO)
        .collect(Collectors.toList());
  }
}
