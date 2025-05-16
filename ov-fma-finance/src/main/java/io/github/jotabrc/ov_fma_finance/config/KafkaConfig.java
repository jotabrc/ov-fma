package io.github.jotabrc.ov_fma_finance.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    public static final String USER_FINANCE_NEW = "user_finance_new";
    public static final String USER_FINANCE_UPDATE = "user_finance_update";
//    public static final String GROUP_ID = "io.github.jotabrc";
//    public static final String SERVERS = "kafka-1:9092";

//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVERS);
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        return new DefaultKafkaConsumerFactory<>(config);
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}
