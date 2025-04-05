package com.denisballa.centralservice;

import com.denisballa.centralservice.config.ThresholdProperties;
import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.service.AlarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AlarmServiceTests {

    private AlarmService alarmService;

    @BeforeEach
    void setup() {
        ThresholdProperties props = new ThresholdProperties();
        props.setTemperature(35);
        props.setHumidity(50);
        alarmService = new AlarmService(props);
    }

    @Test
    void testTemperatureWithinThreshold() {
        SensorMessage msg = new SensorMessage("t1", 30, "temperature");
        alarmService.process(msg);
    }

    @Test
    void testTemperatureExceedsThreshold() {
        SensorMessage msg = new SensorMessage("t2", 40, "temperature");
        alarmService.process(msg);
    }

    @Test
    void testHumidityWithinThreshold() {
        SensorMessage msg = new SensorMessage("h1", 45, "humidity");
        alarmService.process(msg);
    }

    @Test
    void testHumidityExceedsThreshold() {
        SensorMessage msg = new SensorMessage("h2", 55, "humidity");
        alarmService.process(msg);
    }

    @Test
    void testUnknownSensorType() {
        SensorMessage msg = new SensorMessage("x1", 70, "pressure");
        alarmService.process(msg);
    }

}
