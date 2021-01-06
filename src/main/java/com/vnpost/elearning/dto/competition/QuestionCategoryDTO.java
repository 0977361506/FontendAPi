package com.vnpost.elearning.dto.competition;

import com.vnpost.elearning.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCategoryDTO extends AbstractDTO<QuestionCategoryDTO> {

    private String describes;

    private String nameCategory;

    private int shares;

    private Long parent;

    private String pcode;

    //bi-directional many-to-one association to Question

   /* private List<QuestionDTO> questions;


    private List<TreeQuestionDTO> treeQuestions1;


    private List<TreeQuestionDTO> treeQuestions2;*/
//   private PoscodeVnpostDTO poscodeVnpost;
}
