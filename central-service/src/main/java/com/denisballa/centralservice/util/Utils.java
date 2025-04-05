package com.denisballa.centralservice.util;

import java.util.Optional;
import com.denisballa.centralservice.model.SensorMessage;

/**
 * Utility class
 */
public class Utils {

    private Utils() {} // prevent instantiation

    /**
     * Checks if the given SensorMessage is valid.
     *
     * @param message the SensorMessage to check
     * @return true if the message is valid, false otherwise
     */
    public static boolean isValidSensorMessage(SensorMessage message) {
        return message != null &&
                message.getSensorId() != null &&
                message.getValue() >= 0 &&
                (message.getType().equalsIgnoreCase("temperature") ||
                        message.getType().equalsIgnoreCase("humidity"));
    }

    /**
     * Checks if the sensor value exceeds the specified threshold.
     *
     * @param type            the type of the sensor (e.g., "temperature", "humidity")
     * @param value           the value of the sensor
     * @param tempThreshold   the temperature threshold
     * @param humidityThreshold the humidity threshold
     * @return true if the value exceeds the threshold, false otherwise
     */
    public static boolean exceedsThreshold(String type, int value, int tempThreshold, int humidityThreshold) {
        return switch (type.toLowerCase()) {
            case "temperature" -> value > tempThreshold;
            case "humidity" -> value > humidityThreshold;
            default -> false;
        };
    }

    /**
     * Formats a SensorMessage into a readable string.
     *
     * @param message the SensorMessage to format
     * @return a formatted string representation of the SensorMessage
     */
    public static String formatSensorLog(SensorMessage message) {
        return String.format("Sensor: %s | Type: %s | Value: %d",
                message.getSensorId(),
                message.getType().toUpperCase(),
                message.getValue());
    }

    /**
     * Parses a raw sensor message string into a SensorMessage object.
     *
     * @param raw  the raw sensor message string
     * @param type the type of the sensor (e.g., "temperature", "humidity")
     * @return an Optional containing the SensorMessage if parsing was successful, or an empty Optional if it failed
     */
    public static Optional<SensorMessage> parseMessage(String raw, String type) {
        try {
            String[] parts = raw.split(";");
            String sensorId = parts[0].split("=")[1];
            int value = Integer.parseInt(parts[1].split("=")[1]);
            return Optional.of(new SensorMessage(sensorId, value, type));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
