package com.denisballa.centralservice.kafka.consumer;

import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.service.AlarmService;
import com.denisballa.centralservice.service.LogService;
import com.denisballa.centralservice.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for processing sensor data messages.
 * It listens to the "sensor-data" topic and processes incoming messages.
 */
@Slf4j
@Component
public class SensorDataConsumer {

    private final LogService logService;
    private final AlarmService alarmService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SensorDataConsumer(LogService logService, AlarmService alarmService) {
        this.alarmService = alarmService;
        this.logService = logService;
    }

    /**
     * Consumes messages from the Kafka topic and processes them.
     *
     * @param record the Kafka consumer record containing the message
     */
    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            SensorMessage message = objectMapper.readValue(record.value(), SensorMessage.class);
            if (!Utils.isValidSensorMessage(message)) {
                log.warn("⚠️ Received invalid sensor message: {}", record.value());
                return;
            }
            logService.log(message);
            alarmService.process(message);
        } catch (Exception e) {
            log.error("❌ Failed to process Kafka message: {}", record.value(), e);
        }
    }

}
