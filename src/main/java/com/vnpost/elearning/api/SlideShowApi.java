package com.vnpost.elearning.api;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.service.SlideShowService;
import eln.common.core.entities.config.SlideShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/slideshow")
public class SlideShowApi {

    @Autowired
    public SlideShowService slideShowService;

    @GetMapping("/{code}")
    public ResponseEntity<ServiceResult> getSlideShowByCode(@PathVariable String code) {
        List<SlideShow> slideShows = slideShowService.findAllByCode(code);
        return ResponseEntity.ok(new ServiceResult(slideShows,"success","200"));
    }
}
