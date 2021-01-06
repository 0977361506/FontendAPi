package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ConditionDTO extends AbstractDTO<ConditionDTO> {

    private Date lastUpdate;

    private String nameCondition;
    private Date timeCreate;
  /*  private List<RoundTestDTO> roundTests;*/

}
