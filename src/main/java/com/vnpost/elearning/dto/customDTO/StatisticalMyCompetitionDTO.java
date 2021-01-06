package com.vnpost.elearning.dto.customDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticalMyCompetitionDTO {
    private Integer qualified;
    private Integer imqualified;
    private Integer notDoExam;
    private Integer countTopThreeRound;
    private Double percentMediumQuestion;
    private Double percentMediumPoint;
    private Integer  waitingConfirm;

    public StatisticalMyCompetitionDTO(Integer qualified, Integer imqualified, Integer notDoExam, Integer countTopThreeRound, Double percentMediumQuestion, Double percentMediumPoint, Integer waitingConfirm) {
        this.qualified = qualified;
        this.imqualified = imqualified;
        this.notDoExam = notDoExam;
        this.countTopThreeRound = countTopThreeRound;
        this.percentMediumQuestion = percentMediumQuestion;
        this.percentMediumPoint = percentMediumPoint;
        this.waitingConfirm = waitingConfirm;
    }
}
