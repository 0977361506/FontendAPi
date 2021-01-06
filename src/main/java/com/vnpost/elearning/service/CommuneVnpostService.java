package com.vnpost.elearning.service;

import com.vnpost.elearning.dto.CommuneVnpostDTO;
import eln.common.core.entities.unit.CommuneVnpost;
import eln.common.core.repository.CommuneVnpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommuneVnpostService {

    @Autowired
    private CommuneVnpostRepository communeVnpostRepository;

    public List<CommuneVnpostDTO> findByDistrictId(Long id) {
        List<CommuneVnpost> entities = communeVnpostRepository.findAllByDistrictVnpostId(id);
        List<CommuneVnpostDTO> result = new ArrayList<>();
        for (CommuneVnpost entity : entities) {
            CommuneVnpostDTO dto = new CommuneVnpostDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            result.add(dto);
        }
        return result;
    }

    public CommuneVnpost findById(Long id) {
        return communeVnpostRepository.findById(id).get();
    }
}
