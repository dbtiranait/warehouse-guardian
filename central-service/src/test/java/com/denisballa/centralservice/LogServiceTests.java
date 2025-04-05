package com.denisballa.centralservice;

import com.denisballa.centralservice.model.SensorMessage;
import com.denisballa.centralservice.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LogServiceTests {

    private LogService logService;

    @BeforeEach
    void setUp() {
        logService = new LogService();
    }

    @Test
    void testLog_validMessage() {
        SensorMessage msg = new SensorMessage("s1", 25, "temperature");
        assertDoesNotThrow(() -> logService.log(msg));
    }

}