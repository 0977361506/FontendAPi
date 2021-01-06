package com.vnpost.elearning.dto.competition;


import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddPointDTO extends AbstractDTO<AddPointDTO> {




    private int addPoint;

    private Date lastUpdate;

    private Date timeCreate;

     private RoundTestDTO roundTest;


    /*private TypeQuestionDTO typeQuestion;*/
    private LevellDTO levellDTO;

}
