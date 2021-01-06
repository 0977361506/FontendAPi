package com.vnpost.elearning.dto.customDTO;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CustomQRoundTestDTO implements Serializable {
    private String idQuestion;
    private List<String> answerChecked;
    private List<String> answerNotChecked;
    private String typeQuestion;
    //private Date timeStart;
    private Long couseId;
}
