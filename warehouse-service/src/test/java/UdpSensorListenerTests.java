import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import com.denisballa.warehouse.listener.UdpSensorListener;
import com.denisballa.warehouse.model.SensorMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UdpSensorListenerTests {

    private static final KafkaSensorProducer producer = new KafkaSensorProducer();
    private static final UdpSensorListener temperatureSensorListener = new com.denisballa.warehouse.listener.UdpSensorListener("temperature", producer);
    private static final UdpSensorListener humiditySensorListener = new com.denisballa.warehouse.listener.UdpSensorListener("humidity", producer);

    @Test
    void testValidTemperatureMessage() {
        String input = "sensor_id=t1;value=36";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNotNull(message);
        assertEquals("t1", message.getSensorId());
        assertEquals(36, message.getValue());
        assertEquals("temperature", message.getType());
    }

    @Test
    void testValidHumidityMessage() {
        String input = "sensor_id=h1;value=45";
        SensorMessage message = humiditySensorListener.parseMessage(input);
        assertNotNull(message);
        assertEquals("h1", message.getSensorId());
        assertEquals(45, message.getValue());
        assertEquals("humidity", message.getType());
    }

    @Test
    void testInvalidMessageFormat() {
        String input = "bad_input_format";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNull(message);
    }

    @Test
    void testMissingSensorId() {
        String input = "value=30";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNull(message);
    }

    @Test
    void testMissingValue() {
        String input = "sensor_id=t2";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNull(message);
    }

    @Test
    void testValueNotAnInteger() {
        String input = "sensor_id=t2;value=abc";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNull(message);
    }

    @Test
    void testEmptyMessage() {
        String input = "";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNull(message);
    }

    @Test
    void testNullMessage() {
        SensorMessage message = temperatureSensorListener.parseMessage(null);
        assertNull(message);
    }

    @Test
    void testWhitespaceMessage() {
        String input = "   ";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNull(message);
    }

    @Test
    void testExtraPartsIgnored() {
        String input = "sensor_id=t1;value=40;extra=foo";
        SensorMessage message = temperatureSensorListener.parseMessage(input);
        assertNotNull(message);
        assertEquals("t1", message.getSensorId());
        assertEquals(40, message.getValue());
    }

    @Test
    void testStartReceivesPacketAndProcessesIt() throws Exception {
        // Arrange
        KafkaProducer<String, String> mockKafka = mock(KafkaProducer.class);
        KafkaSensorProducer mockProducer = new KafkaSensorProducer(mockKafka, "sensor-data");

        // Bind a local UDP socket
        DatagramSocket socket = new DatagramSocket(9999); // any available port
        UdpSensorListener listener = new UdpSensorListener("test", 9999, mockProducer, socket);

        listener.start();

        // Send a test UDP packet
        byte[] data = "sensor_id=test1;value=99".getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 9999);
        DatagramSocket senderSocket = new DatagramSocket();
        senderSocket.send(packet);
        senderSocket.close();

        Thread.sleep(500); // allow background thread to process

        listener.stop();
        verify(mockKafka, atLeastOnce()).send(any());

    }

}
