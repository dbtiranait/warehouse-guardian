package com.denisballa.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a sensor message with an ID, value, and type.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMessage {
    /*
    * The ID of the sensor that generated the message.
     */
    private String sensorId;
    /*
    * The value reported by the sensor.
     */
    private int value;
    /*
    * The type of the sensor (e.g., temperature, humidity).
     */
    private String type;
}