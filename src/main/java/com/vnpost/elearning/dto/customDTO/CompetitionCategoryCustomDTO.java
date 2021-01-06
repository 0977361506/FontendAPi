package com.vnpost.elearning.dto.customDTO;


import com.vnpost.elearning.dto.competition.CompetionCategoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class CompetitionCategoryCustomDTO implements Serializable {
    private List<CompetionCategoryDTO> competitionDTOList;
    private Integer count;

}
