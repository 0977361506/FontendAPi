package com.vnpost.elearning.dto.customDTO;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CustomProcessCompeDTO implements Serializable {
    Integer point;
    Integer result;
    Integer status;
    Integer confirm;
    Integer totalQuestion;
    Integer sumPoint;
    Boolean configed;


}
