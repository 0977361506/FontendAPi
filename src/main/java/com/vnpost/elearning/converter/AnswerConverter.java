package com.vnpost.elearning.converter;


import com.vnpost.elearning.dto.competition.AnswerDTO;
import eln.common.core.entities.question.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnswerConverter implements IDTO<AnswerDTO>, IEntity<Answer> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AnswerDTO convertToDTO(Object entity) {

        AnswerDTO answerDTO = modelMapper.map(entity, AnswerDTO.class);
        return answerDTO;
    }
    public List<AnswerDTO> convertToList(List<Answer> a){
        List<AnswerDTO> answerDTOS = new ArrayList<>();
        for(Answer b :a){
            answerDTOS.add(convertToDTO(b));
        }
        return  answerDTOS ;
    }

    @Override
    public Answer convertToEntity(Object dto) {

        Answer answer = modelMapper.map(dto, Answer.class);
        return answer;
    }
}
