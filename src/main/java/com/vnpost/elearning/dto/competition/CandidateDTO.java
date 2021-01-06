package com.vnpost.elearning.dto.competition;


import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CandidateDTO extends AbstractDTO<CandidateDTO> {
    private static final long serialVersionUID = 1L;


    private Date lastUpdate;

    private Integer status;

    private Date timeCreate;

    private Integer counttest;

    private Date timestart;

    private Integer point;
    private Date timeend;

    private Integer lockCandidate;
    private Integer result;
    private Integer totalQuestion;

    //bi-directional many-to-one association to GroupTest

    private GroupTestDTO groupTest;

    //bi-directional many-to-one association to RoundTest

    private RoundTestDTO roundTest;
    private  Integer sumPoint;

    private UsersDTO user;


    private String nameUser;
    private String idGroupMember;
    private String idStatusTestMember;
    private String id_round;

    private String sumCounttest;
    private String mediumPoint;
    private String maxPoint;

    private String nameParentPoscode;


}
