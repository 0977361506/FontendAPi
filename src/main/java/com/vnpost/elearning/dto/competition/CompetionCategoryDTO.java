package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CompetionCategoryDTO extends AbstractDTO<CompetionCategoryDTO> {

	private String describes;
	private Date lastUpdate; 
	private String nameCompetition;
	private Date timeCreate;
	private Long parent;

	private Integer status;
}
