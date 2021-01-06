package com.vnpost.elearning.Beans;


import eln.common.core.entities.competition.RoundTest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CheckPermistion {

    public boolean checkDoAgain(RoundTest roundTest,Integer counttest){
        if(roundTest.getDoAgain()==0 && roundTest.getMaxWork()==0){
            return true;
        }else if(roundTest.getDoAgain()==1 && counttest>roundTest.getMaxWork() ){
           return false;
        } else if(roundTest.getMaxWork()==0){
          return true;
        } else if(roundTest.getMaxWork()!=0){
            if(counttest-roundTest.getMaxWork()>0){
                return false;
            }
        }
        return true;
    }

    public boolean checkAll(RoundTest roundTest, Date date,Integer counttest){
         if(!checkDoAgain( roundTest, counttest)){
                return false;
         }
        if(!checkDateDuration(  roundTest,   date)){
            return false;
        }
        return true;
    }


    public boolean
    checkDateDuration(RoundTest roundTest, Date date){

        if(roundTest.getCompetition().getTimeStart()==null){
            if(roundTest.getTimeStart()==null){
                return true;
            }else
            if (date.compareTo(roundTest.getTimeStart()) >= 0
                    && date.compareTo(roundTest.getTimeEnd()) <= 0) {
                return true;
            }else {
                return false;
            }
        }else{

            if (date.compareTo(roundTest.getCompetition().getTimeStart()) >= 0
                    && date.compareTo(roundTest.getCompetition().getTimeEnd()) <= 0) {
                if (roundTest.getTimeStart() == null) {
                    return true;
                } else if (date.compareTo(roundTest.getTimeStart()) >= 0
                        && date.compareTo(roundTest.getTimeEnd()) <= 0) {
                    return true;
                } else {
                    return false;
                }
            }else {
                return false;
            }

        }

    }

}
