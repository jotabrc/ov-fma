package io.github.jotabrc.ov_fma_auth.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jotabrc.ov_fma_auth.config.KafkaConfig;
import io.github.jotabrc.ov_fma_auth.dto.UserDto;
import io.github.jotabrc.ov_fma_auth.service.AuthService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {

    private final AuthService authService;

    @Autowired
    public KafkaEventConsumer(AuthService authService) {
        this.authService = authService;
    }

    @KafkaListener(topics = {KafkaConfig.USER_NEW, KafkaConfig.USER_UPDATE},
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(ConsumerRecord<String, String> record) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserDto userDto = objectMapper.readValue(record.value(), UserDto.class);

        switch (record.topic()) {
            case KafkaConfig.USER_NEW -> authService.save(userDto);
            case KafkaConfig.USER_UPDATE -> authService.update(userDto);
        }
    }
}
