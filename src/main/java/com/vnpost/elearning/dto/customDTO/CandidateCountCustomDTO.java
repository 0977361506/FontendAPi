package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CandidateCountCustomDTO extends AbstractDTO<CandidateCountCustomDTO> {
    private String countTime;
    private Date timeStart;
    private Integer point;
}
