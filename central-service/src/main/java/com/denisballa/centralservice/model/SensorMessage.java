package com.denisballa.centralservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorMessage {
    private String sensorId;
    private int value;
    private String type; // "temperature" or "humidity"
}