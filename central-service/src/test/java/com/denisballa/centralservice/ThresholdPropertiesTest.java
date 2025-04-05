package com.denisballa.centralservice;

import com.denisballa.centralservice.config.ThresholdProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThresholdPropertiesTest {

    @Test
    void testGetterSetter() {
        ThresholdProperties props = new ThresholdProperties();
        props.setTemperature(40);
        props.setHumidity(60);
        assertEquals(40, props.getTemperature());
        assertEquals(60, props.getHumidity());
    }

}
