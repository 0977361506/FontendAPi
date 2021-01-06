package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadDTO {
    private MultipartFile multipartFile;

    private String src;
    public String base64;
    public String extension;
    public String name;

}
