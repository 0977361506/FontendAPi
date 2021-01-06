package com.vnpost.elearning.api;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/page")
public class PageApi {

    @Autowired
    private StaticPageService staticPageService;

    @GetMapping("/{code}")
    public ResponseEntity<ServiceResult> getPageByCode(@PathVariable String code) {
        return ResponseEntity.ok(new ServiceResult(staticPageService.findByCode(code),"success","200" ) );
    }
}
