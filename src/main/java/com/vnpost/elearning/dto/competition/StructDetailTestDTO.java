package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StructDetailTestDTO  extends AbstractDTO<StructDetailTestDTO> {


    private int countTest;




    private Date lastUpdate;

    private String nameGroup;

    private Date timeCreate;



    private LevellDTO levell;


    private TagDTO tag;


    private QuestionCategoryDTO questionCategoryStruct;



    private TypeQuestionDTO typeQuestion;


    private StructTestDTO structTestDetail;





    private String idlevell;
    private String idtag;
    private String idquestionCategoryStruct;
    private String idtypeQuestion;
    private String idstructTestDetail;

}
