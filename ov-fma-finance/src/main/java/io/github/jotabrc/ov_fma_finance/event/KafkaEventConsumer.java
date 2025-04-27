package io.github.jotabrc.ov_fma_finance.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jotabrc.ov_fma_finance.config.KafkaConfig;
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

    @KafkaListener(topics = {KafkaConfig.USER_FINANCE_NEW},
            groupId = KafkaConfig.GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
    public void listener(ConsumerRecord<String, String> record) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserFinanceDto dto = objectMapper.readValue(record.value(), UserFinanceDto.class);

        financeService.addUserFinance(dto);
    }
}
