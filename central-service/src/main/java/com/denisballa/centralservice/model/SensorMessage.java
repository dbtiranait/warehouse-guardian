package com.denisballa.centralservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a message from a sensor.
 * Contains the sensor ID, the value of the measurement, and the type of measurement (temperature or humidity).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMessage {
    private String sensorId;
    private int value;
    private String type; // "temperature" or "humidity"
}