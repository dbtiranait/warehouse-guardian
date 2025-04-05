package com.denisballa.centralservice;

import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.util.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UtilsTests {

    @Test
    void testParseMessage_validInput() {
        String raw = "sensor_id=s1;value=42";
        String type = "temperature";
        Optional<SensorMessage> result = Utils.parseMessage(raw, type);
        assertTrue(result.isPresent());
        SensorMessage message = result.get();
        assertEquals("s1", message.getSensorId());
        assertEquals(42, message.getValue());
        assertEquals("temperature", message.getType());
    }

    @Test
    void testParseMessage_invalidFormat() {
        String raw = "bad_format_string";
        Optional<SensorMessage> result = Utils.parseMessage(raw, "humidity");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExceedsThreshold_temperature() {
        assertTrue(Utils.exceedsThreshold("temperature", 40, 35, 50));
        assertFalse(Utils.exceedsThreshold("temperature", 34, 35, 50));
    }

    @Test
    void testExceedsThreshold_humidity() {
        assertTrue(Utils.exceedsThreshold("humidity", 60, 35, 50));
        assertFalse(Utils.exceedsThreshold("humidity", 45, 35, 50));
    }

    @Test
    void testFormatSensorLog() {
        SensorMessage message = new SensorMessage("h2", 48, "humidity");
        String log = Utils.formatSensorLog(message);
        assertTrue(log.contains("h2"));
        assertTrue(log.contains("HUMIDITY"));
        assertTrue(log.contains("48"));
    }

    @Test
    void testIsValidSensorMessage_valid() {
        SensorMessage msg = new SensorMessage("t1", 35, "temperature");
        assertTrue(Utils.isValidSensorMessage(msg));
    }

    @Test
    void testIsValidSensorMessage_invalidType() {
        SensorMessage msg = new SensorMessage("x1", 50, "pressure");
        assertFalse(Utils.isValidSensorMessage(msg));
    }

    @Test
    void testIsValidSensorMessage_missingId() {
        SensorMessage msg = new SensorMessage(null, 25, "humidity");
        assertFalse(Utils.isValidSensorMessage(msg));
    }

}
