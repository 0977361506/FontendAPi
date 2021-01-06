package com.vnpost.elearning.dto.customDTO;

import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.competition.TestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TestCustomDTO  extends AbstractDTO<TestCustomDTO> {

    List<TestDTO> testDTOList;
    Integer count;

}
