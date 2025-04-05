package com.denisballa.centralservice;

import com.denisballa.centralservice.kafka.consumer.SensorDataConsumer;
import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.service.AlarmService;
import com.denisballa.centralservice.service.LogService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class SensorDataConsumerTests {

    private LogService mockLogService;
    private AlarmService mockAlarmService;
    private SensorDataConsumer consumer;

    @BeforeEach
    void setUp() {
        mockLogService = Mockito.mock(LogService.class);
        mockAlarmService = Mockito.mock(AlarmService.class);
        consumer = new SensorDataConsumer(mockLogService, mockAlarmService);
    }

    @Test
    void testConsume_validMessage() {
        // Arrange
        String json = "{\"sensorId\":\"t1\",\"value\":36,\"type\":\"temperature\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("sensor-data", 0, 0L, "t1", json);

        // Act
        consumer.consume(record);

        // Assert
        verify(mockLogService, times(1)).log(any(SensorMessage.class));
        verify(mockAlarmService, times(1)).process(any(SensorMessage.class));

    }

    @Test
    void testConsume_invalidJson() {
        String badJson = "not-a-json";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("sensor-data", 0, 0L, "x1", badJson);

        // Act
        consumer.consume(record);

        // Assert
        verify(mockLogService, never()).log(any());
        verify(mockAlarmService, never()).process(any());
    }

    @Test
    void testConsume_invalidSensorMessage() {
        String json = "{\"sensorId\":null,\"value\":20,\"type\":\"humidity\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("sensor-data", 0, 0L, null, json);

        // Act
        consumer.consume(record);

        // Assert
        verify(mockLogService, never()).log(any());
        verify(mockAlarmService, never()).process(any());
    }

}

