package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TagDTO extends AbstractDTO<TagDTO> {



    private Date lastUpdate;

    private String name;

    private Date timeCreate;

}


