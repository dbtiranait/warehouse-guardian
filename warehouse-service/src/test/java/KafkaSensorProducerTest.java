
import com.denisballa.warehouse.model.SensorMessage;
import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KafkaSensorProducerTest {

    private KafkaProducer<String, String> mockProducer;
    private KafkaSensorProducer producer;
    private final String testTopic = "test-topic";

    @BeforeEach
    void setUp() {
        mockProducer = mock(KafkaProducer.class);
        producer = new KafkaSensorProducer(mockProducer, testTopic);
    }

    @Test
    void testSendSensorMessage_sendsToKafkaWithCorrectJson() throws Exception {
        // Arrange
        SensorMessage message = new SensorMessage("t1", 42, "temperature");
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(message);

        ArgumentCaptor<ProducerRecord<String, String>> recordCaptor =
                ArgumentCaptor.forClass(ProducerRecord.class);

        // Act
        producer.sendSensorMessage(message);

        // Assert
        verify(mockProducer, times(1)).send(recordCaptor.capture());
        ProducerRecord<String, String> captured = recordCaptor.getValue();

        assertEquals("t1", captured.key());
        assertEquals(expectedJson, captured.value());
        assertEquals(testTopic, captured.topic());

    }

}