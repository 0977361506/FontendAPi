package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.MailConfigDTO;
import com.vnpost.elearning.dto.NewCategoryDTO;
import eln.common.core.entities.news.NewCategory;
import eln.common.core.entities.user.MailConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailConfigConverter  implements IDTO<MailConfigDTO>, IEntity<MailConfig>  {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MailConfigDTO convertToDTO(Object entity) {
        MailConfig mailConfig = (MailConfig) entity;
        MailConfigDTO newCategoryDTO = modelMapper.map(mailConfig, MailConfigDTO.class);
        return newCategoryDTO;
    }

    @Override
    public MailConfig convertToEntity(Object dto) {
        MailConfigDTO mailConfigDTO = (MailConfigDTO) dto;
        MailConfig mailConfig = modelMapper.map(mailConfigDTO, MailConfig.class);
        return mailConfig;
    }
}
