package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.QuestionTestConverter;
import eln.common.core.repository.QuestionTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service
public class QuestionTestService {

    @Autowired
    private QuestionTestRepository questionTestRepository;
    @Autowired
    private QuestionTestConverter questionTestConverter;


    public Integer countByIdTest(String idTest) {
        return questionTestRepository.countByIdTest(idTest);
    }


    public void deleteById(String id) {
        questionTestRepository.deleteById(id);
    }


    public String saveQuestionTest(String id_struct_detail, String id_test, String id_question) {
        try {
            Integer count = questionTestRepository.countByidQuestionIdTest(id_question, id_test);
            if (count <= 0) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time_create_time_update = dateFormat.format(java.util.Calendar.getInstance().getTime());
                questionTestRepository.saveQuestionForTest(id_question, id_test, id_struct_detail, time_create_time_update, time_create_time_update);

                return "0";
            } else {
                return "1";
            }
        } catch (Exception e) {
            return "1";
        }
    }
}
