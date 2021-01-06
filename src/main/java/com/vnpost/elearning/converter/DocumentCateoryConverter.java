package com.vnpost.elearning.converter;

import com.vnpost.elearning.dto.DocumentCategoryDTO;
import eln.common.core.entities.events.DocumentCategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentCateoryConverter implements IDTO<DocumentCategoryDTO>, IEntity<DocumentCategory> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DocumentCategoryDTO convertToDTO(Object entity) {
        DocumentCategory documentCategory = (DocumentCategory) entity;
        DocumentCategoryDTO dto = modelMapper.map(documentCategory, DocumentCategoryDTO.class);
        return dto;
    }
    public List<DocumentCategoryDTO> convertToList(List<DocumentCategory> categories){
        List<DocumentCategoryDTO> categories1 = new ArrayList<>();
        for(DocumentCategory c: categories){
            categories1.add(convertToDTO(c));
        }
        return  categories1;
    }

    @Override
    public DocumentCategory convertToEntity(Object dto) {
        DocumentCategoryDTO documentCategoryDTO = (DocumentCategoryDTO) dto;
        DocumentCategory entity = modelMapper.map(documentCategoryDTO, DocumentCategory.class);
        return entity;
    }


}
