import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import com.denisballa.warehouse.model.SensorMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UdpSensorListener {

    private static final KafkaSensorProducer producer = new KafkaSensorProducer();
    private static final com.denisballa.warehouse.listener.UdpSensorListener temperatureSensorListener = new com.denisballa.warehouse.listener.UdpSensorListener("temperature", producer);
    private static final com.denisballa.warehouse.listener.UdpSensorListener humiditySensorListener = new com.denisballa.warehouse.listener.UdpSensorListener("humidity", producer);

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

}
