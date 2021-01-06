package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PoscodeCompetitionDTO extends AbstractDTO<PoscodeCompetitionDTO> {
    private Date timeUpdate;
    private Date timeCreate;
    private PoscodeVnpostDTO poscodeVnpost;
    private CompetitionDTO competition;
}
