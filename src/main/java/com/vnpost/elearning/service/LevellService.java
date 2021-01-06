package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.LevellConverter;
import com.vnpost.elearning.dto.competition.LevellDTO;
import eln.common.core.entities.question.Levell;
import eln.common.core.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LevellService {
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private LevellConverter levellConverter;


    public List<LevellDTO> getListAll() {
        List<Levell> list = levelRepository.getListAll();
        List<LevellDTO> levellDTOList = new ArrayList<>();
        for (Levell levell : list) {
            LevellDTO levellDTO = levellConverter.convertToDTO(levell);
            levellDTOList.add(levellDTO);
        }
        return levellDTOList;
    }


    public Levell findById(Long id) {
        return levelRepository.findById(id).get();
    }
}
