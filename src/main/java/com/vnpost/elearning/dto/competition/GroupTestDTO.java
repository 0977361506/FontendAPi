package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupTestDTO extends AbstractDTO<GroupTestDTO> {
    private static final long serialVersionUID = 1L;
    private String nameGroup;
    private RoundTestDTO roundTest;





    private String countStudent;
}
