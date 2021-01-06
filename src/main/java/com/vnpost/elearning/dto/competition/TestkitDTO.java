package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TestkitDTO extends AbstractDTO<TestkitDTO> {


 
	 
	private String describes;
 
	private Date lastUpdate;
 
	private String nameTest;
 
	private Date timeCreate;

	private Long parent;

	private Integer status;
}
