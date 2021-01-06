package com.vnpost.elearning.service;

import eln.common.core.entities.config.SlideShow;
import eln.common.core.repository.SlideShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlideShowService {

    @Autowired
    private SlideShowRepository slideShowRepository;


    public SlideShow save(SlideShow slideShow) {
        return slideShowRepository.save(slideShow);
    }

//    @Override
//    public List<SlideShow> distinctCode() {
//        return slideShowRepository.findDistinct();
//    }


    public List<SlideShow> findAll() {
        return slideShowRepository.findAll();
    }


    public List<SlideShow> findAllByCode(String code) {
        return slideShowRepository.findAllByCode(code);
    }


    public void delete(Long id) {
        slideShowRepository.deleteById(id);
    }


    public void deleteAllByCode(String code) {
        slideShowRepository.deleteAll(slideShowRepository.findAllByCode(code));
    }
}
