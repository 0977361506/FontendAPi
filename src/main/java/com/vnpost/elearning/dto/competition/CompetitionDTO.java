package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import com.vnpost.elearning.dto.customDTO.CustomProcessCompeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CompetitionDTO extends AbstractDTO<CompetitionDTO> {

    private String describe;


    private PoscodeVnpostDTO poscodeVnpost;

    private String imageCompetition;

    private String nameCompetition;

    private int statusCompetition;

    private int highlight;

    private Date lastUpdate;
    private Date timeCreate;

    private Date timeEnd;

    private Date timeStart;

    private CompetionCategoryDTO competitionCategory;

    private Integer checkcourseware;

    private String category_value;
    private String status_search_value;
    private String status_search_statusCompetition;
    private String id_unit;

    private String countCandiates;

    List<RoundTestDTO> qRoundTestDTOList;
    List<CustomProcessCompeDTO> customProcessCompeDTOs;
    private Long categoryId;
    private Integer checkFinish;
    private String typeMyCompetition;

}
