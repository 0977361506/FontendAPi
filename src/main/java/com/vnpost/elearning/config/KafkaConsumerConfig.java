package com.vnpost.elearning.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 07,2020
 */
// @EnableKafka
@Configuration
public class KafkaConsumerConfig {
  //  @Autowired private KafkaProperties kafkaProperties;
  //
  //  @Bean
  //  public ConsumerFactory<String, Object> consumerFactory() {
  //    Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
  //    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
  //    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
  //    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
  //    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  //    return new DefaultKafkaConsumerFactory<>(props);
  //  }
  //
  //  @Bean
  //  public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory()
  // {
  //    ConcurrentKafkaListenerContainerFactory<String, Object> factory =
  //        new ConcurrentKafkaListenerContainerFactory<>();
  //    factory.setConsumerFactory(consumerFactory());
  //    return factory;
  //  }
}
