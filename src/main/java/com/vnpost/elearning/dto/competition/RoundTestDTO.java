package com.vnpost.elearning.dto.competition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vnpost.elearning.dto.AbstractDTO;
import com.vnpost.elearning.dto.PoscodeVnpostDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoundTestDTO extends AbstractDTO<RoundTestDTO> {
    private int autoCreateQuestion;
	
	@JsonIgnore
    private String codeRoundTest;

    private String describes;

    private Integer doAgain;

    private Integer fullTickAnswer;

    private Integer giveCertificate;

    private Date lastUpdate;

    private Integer maxPoint;

    private Integer maxWork;

    private Integer minPoint;

    private Integer mixAnswer;

    private String nameRound;

    private Integer showResutl;
    private Integer showAnswer;

    private Integer showExplain;

    private Integer sourceQuestion;

    private Integer statusRound;

    private Date timeCreate;

    private Date timeEnd;

    private Integer timeRound;

    private Date timeStart;


    private CompetitionDTO competition;

    private ConditionDTO condition;
    @JsonIgnore
    private MixCompetitionDTO mixCompetition;
    @JsonIgnore
    private StructTestDTO structTest;
    @JsonIgnore
    private PoscodeVnpostDTO poscodeVnpostRound;

    private Integer existUser;

    private String countQuestion;




}
