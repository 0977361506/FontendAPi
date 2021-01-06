package com.vnpost.elearning.api;

import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.service.ConfigurationnService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/param")
@AllArgsConstructor
public class ParamApi {
    private ConfigurationnService configurationnService;

    @GetMapping("/{codeName}")
    public ResponseEntity<ServiceResult> getParamByCodeName(@PathVariable String codeName) {
        ServiceResult result = new ServiceResult();
        result.setCode("200");
        result.setMessage("success");
        result.setData(configurationnService.findByCodeNameAndStatus(codeName));
        return ResponseEntity.ok(result);
    }
}
