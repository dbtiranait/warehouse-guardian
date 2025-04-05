package com.denisballa.centralservice;

import com.denisballa.centralservice.model.SensorMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SensorMessageTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        SensorMessage message = new SensorMessage("s1", 45, "temperature");
        assertEquals("s1", message.getSensorId());
        assertEquals(45, message.getValue());
        assertEquals("temperature", message.getType());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        SensorMessage message = new SensorMessage();
        message.setSensorId("s2");
        message.setValue(30);
        message.setType("humidity");
        assertEquals("s2", message.getSensorId());
        assertEquals(30, message.getValue());
        assertEquals("humidity", message.getType());
    }

    @Test
    void testToStringContainsFields() {
        SensorMessage message = new SensorMessage("s3", 28, "temperature");
        String str = message.toString();
        assertTrue(str.contains("s3"));
        assertTrue(str.contains("28"));
        assertTrue(str.toLowerCase().contains("temperature"));
    }

    @Test
    void testEqualsAndHashCode() {
        SensorMessage a = new SensorMessage("s1", 20, "temperature");
        SensorMessage b = new SensorMessage("s1", 20, "temperature");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

}