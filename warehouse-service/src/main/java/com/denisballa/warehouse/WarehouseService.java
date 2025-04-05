package com.denisballa.warehouse;

import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import com.denisballa.warehouse.listener.UdpSensorListener;

import java.util.Stack;

/*
 * This is the main class for the warehouse service. It creates two UDP listeners
 * for temperature and humidity sensors and starts them.
 *
 * The listeners will send the sensor data to a Kafka topic.
 */
public class WarehouseService {

    public static void main(String[] args) {

        // Create a Kafka producer to send sensor data to a Kafka topic.
        KafkaSensorProducer producer = new KafkaSensorProducer();

        // Create two UDP listeners for temperature and humidity sensors.
        UdpSensorListener temperatureListener = new UdpSensorListener("temperature", producer);
        UdpSensorListener humidityListener = new UdpSensorListener("humidity", producer);

        // Start the UDP listeners. This will start listening for incoming UDP packets
        temperatureListener.start();
        humidityListener.start();

        // Add shutdown hook to close the producer when the application is terminated
        // This ensures that all messages are sent before the application exits
        // and that the producer is properly closed.
        // This is important for resource management and to avoid data loss.
        // The shutdown hook is a thread that is executed when the JVM is shutting down.
        // It is a good practice to close resources like producers, consumers, and connections
        // in the shutdown hook to ensure that they are properly cleaned up.
        Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

    }

}
