package com.vnpost.elearning.service;

import com.vnpost.elearning.dto.DistrictVnpostDTO;
import eln.common.core.entities.unit.DistrictVnpost;
import eln.common.core.repository.DistrictVnpostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictVnpostService {

    @Autowired
    private DistrictVnpostRepository districtVnpostRepository;

    public List<DistrictVnpostDTO> findByProvineId(Long id) {
        List<DistrictVnpost> entities = districtVnpostRepository.findAllByProvinceVnpostId(id);
        List<DistrictVnpostDTO> result = new ArrayList<>();
        for (DistrictVnpost entity : entities) {
            DistrictVnpostDTO dto = new DistrictVnpostDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            result.add(dto);
        }
        return result;
    }
}
