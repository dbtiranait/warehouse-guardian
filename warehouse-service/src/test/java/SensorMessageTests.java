import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import com.denisballa.warehouse.listener.UdpSensorListener;
import com.denisballa.warehouse.model.SensorMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorMessageTests {

    private final static KafkaSensorProducer producer = new KafkaSensorProducer();
    private final static UdpSensorListener temperatureSensorListener = new UdpSensorListener("temperature", producer);
    private final static UdpSensorListener humiditySensorListener = new UdpSensorListener("humidity", producer);

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

}
