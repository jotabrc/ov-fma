package io.github.jotabrc.ov_fma_finance.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jotabrc.ov_fma_finance.config.KafkaConfig;
import io.github.jotabrc.ov_fma_finance.dto.UserFinanceAddDto;
import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.service.FinanceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventConsumer {

    private final FinanceService financeService;

    public KafkaEventConsumer(FinanceService financeService) {
        this.financeService = financeService;
    }

    @KafkaListener(topics = {KafkaConfig.USER_FINANCE_NEW, KafkaConfig.USER_FINANCE_UPDATE},
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(ConsumerRecord<String, String> record) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserFinanceAddDto userFinanceAddDto = objectMapper.readValue(record.value(), UserFinanceAddDto.class);

        UserFinanceDto dto = buildNewUserFinanceDto(userFinanceAddDto);

        switch (record.topic()) {
            case KafkaConfig.USER_FINANCE_NEW -> financeService.addUserFinance(dto);
            case KafkaConfig.USER_FINANCE_UPDATE -> financeService.updateUserFinance(dto);
        }
    }

    private UserFinanceDto buildNewUserFinanceDto(final UserFinanceAddDto dto) {
        return UserFinanceDto
                .builder()
                .userUuid(dto.getUserUuid())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .isActive(dto.isActive())
                .build();
    }
}
