import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import com.denisballa.warehouse.model.SensorMessage;
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
        SensorMessage message = new SensorMessage("t1", 42, "temperature");
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(message);

        ArgumentCaptor<ProducerRecord<String, String>> recordCaptor = ArgumentCaptor.forClass(ProducerRecord.class);

        producer.sendSensorMessage(message);

        verify(mockProducer, times(1)).send(recordCaptor.capture());
        ProducerRecord<String, String> captured = recordCaptor.getValue();

        assertEquals("t1", captured.key());
        assertEquals(expectedJson, captured.value());
        assertEquals(testTopic, captured.topic());
    }

    @Test
    void testSendSensorMessage_handlesJsonException() throws Exception {
        // use a spy to throw an exception
        KafkaSensorProducer faultyProducer = new KafkaSensorProducer(mockProducer, testTopic) {
            @Override
            public void sendSensorMessage(SensorMessage message) {
                throw new RuntimeException("Simulated failure");
            }
        };

        assertThrows(RuntimeException.class, () -> faultyProducer.sendSensorMessage(
                new SensorMessage("x", 1, "temperature")));
    }

    @Test
    void testCloseProducer() {
        producer.close();
        verify(mockProducer, times(1)).close();
    }

}
