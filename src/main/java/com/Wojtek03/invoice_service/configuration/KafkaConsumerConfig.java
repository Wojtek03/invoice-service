package com.Wojtek03.invoice_service.configuration;

import com.Wojtek03.invoice_service.dto.OrderEventDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.deadletter.topic}")
    private String deadLetterTopic;

    private final KafkaTemplate<String, OrderEventDto> kafkaTemplate;

    public KafkaConsumerConfig(KafkaTemplate<String, OrderEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, OrderEventDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        JsonDeserializer<OrderEventDto> deserializer = new JsonDeserializer<>(OrderEventDto.class);
        deserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<OrderEventDto> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEventDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        factory.setCommonErrorHandler(new DefaultErrorHandler((consumerRecord, exception) -> {
            log.error("Error processing Kafka message. Record: {}, Error: {}", consumerRecord, exception.getMessage());
            sendToDlt(consumerRecord);
        }));

        return factory;
    }

    private void sendToDlt(ConsumerRecord<?, ?> record) {
        if (record.value() instanceof OrderEventDto event) {
            log.info("Sending record to DLT: {}", event);
            kafkaTemplate.send(deadLetterTopic, event);
        } else {
            log.warn("Received record not being an OrderEventDto in DLT handler: {}", record);
        }
    }
}