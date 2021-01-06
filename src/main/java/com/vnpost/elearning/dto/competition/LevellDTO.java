package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LevellDTO extends AbstractDTO<LevellDTO> {



    private Date lastUpdate;

    private String nameLevell;

    private Date timeCreate;


}
