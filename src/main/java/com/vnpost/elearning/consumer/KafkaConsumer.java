package com.vnpost.elearning.consumer;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 07,2020
 */
@Component
public class KafkaConsumer {
  private final Gson gson = new Gson();
  //    @KafkaListener(topics = "topic-1",groupId = "group-id")
  //    public void listener(String data){
  //        CourseProgressDTO courseProgressDTO = gson.fromJson(data,CourseProgressDTO.class);
  //    System.out.println(courseProgressDTO);
  //    }
}
