package com.vnpost.elearning.dto;

import com.vnpost.elearning.dto.competition.CandidateDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReportCustomDTO implements Serializable {
    private String from;
    private String to;
    private String id_unit;

    List<CandidateDTO> candidateDTOList;
    private String name_unit;
    private String name_competition;
    private Date timestart;
    private Date timeend;
    private String  idCompetition;




}
