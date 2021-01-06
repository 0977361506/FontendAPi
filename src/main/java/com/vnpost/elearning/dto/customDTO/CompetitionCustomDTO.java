package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.competition.CompetitionDTO;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompetitionCustomDTO  extends AbstractDTO<CompetitionCustomDTO> {
    private List<CompetitionDTO> lCompetitions;
    private String countStudents;

    List<RoundTestDTO> roundTestDTOS;



}
