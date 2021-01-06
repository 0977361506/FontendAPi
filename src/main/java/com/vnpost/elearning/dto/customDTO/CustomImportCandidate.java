package com.vnpost.elearning.dto.customDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomImportCandidate  {
    private String email;
    private String name;
    private Integer sex;
    private String birthday;
    private String unit;
    private Boolean status;
}
