package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.NewsDTO;
import eln.common.core.entities.news.New;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsConverter implements IDTO<NewsDTO>, IEntity<New> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public NewsDTO convertToDTO(Object entity) {
        New news = (New) entity;
        NewsDTO newsDTO = modelMapper.map(news, NewsDTO.class);
        if (news.getNewCategory() != null) {
            newsDTO.setId_detail_category(news.getNewCategory().getId());
        }
        newsDTO.setCreatedDate(news.getTimeCreate());
        return newsDTO;
    }

    @Override
    public New convertToEntity(Object dto) {
        NewsDTO newsDTO = (NewsDTO) dto;
        New news = modelMapper.map(newsDTO, New.class);
        return news;
    }
}
