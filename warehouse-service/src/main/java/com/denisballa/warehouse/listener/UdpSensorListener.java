package com.denisballa.warehouse.listener;

import com.denisballa.warehouse.model.SensorMessage;
import com.denisballa.warehouse.config.ConfigLoader;
import com.denisballa.warehouse.kafka.KafkaSensorProducer;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/*
 * This class listens for UDP packets on a specified port and parses the incoming messages.
 * It uses the KafkaSensorProducer to send the parsed messages to a Kafka topic.
 * The messages are expected to be in the format "sensorId=value".
 * The class is initialized with the type of sensor (e.g., temperature, humidity) and the Kafka producer.
 * The port number is loaded from the configuration file using ConfigLoader.
 * The start() method creates a new thread that listens for incoming UDP packets.
 * When a packet is received, it is parsed to extract the sensor ID and value.
 * If the message is valid, it is sent to the Kafka topic using the KafkaSensorProducer.
 * The parseMessage() method handles the parsing of the incoming message.
 * If the message is invalid, it returns null and prints an error message.
 * The class also handles exceptions that may occur during the socket operations.
 */

@Slf4j
public class UdpSensorListener {

    /*
     * The port number on which the UDP listener will listen for incoming packets.
     */
    private final int port;

    /*
     * The type of sensor (e.g., temperature, humidity) that this listener is associated with.
     */
    private final String type;

    /*
     * The Kafka producer used to send the parsed sensor messages to a Kafka topic.
     */
    private final KafkaSensorProducer kafkaProducer;

    /*
     * The DatagramSocket used for receiving UDP packets.
     */
    private final DatagramSocket socket;

    /*
     * A boolean flag indicating whether the listener is currently running.
     */
    private boolean running = false;

    /*
     * Constructor for the UdpSensorListener class.
     * Initializes the type of sensor, port number, and Kafka producer.
     * The port number is loaded from the configuration file using ConfigLoader.
     * @param type The type of sensor (e.g., temperature, humidity).
     * @param producer The Kafka producer used to send the parsed sensor messages.
     */
    public UdpSensorListener(String type, KafkaSensorProducer producer) {
        this.type = type;
        this.port = ConfigLoader.getInt("sensor." + type + ".port");
        this.kafkaProducer = producer;
        this.socket = null; // created at runtime in start()
    }

    // Overloaded constructor (used in tests)
    public UdpSensorListener(String type, int port, KafkaSensorProducer producer, DatagramSocket socket) {
        this.type = type;
        this.port = port;
        this.kafkaProducer = producer;
        this.socket = socket;
    }

    /*
     * Starts the UDP listener in a new thread.
     * Listens for incoming UDP packets on the specified port.
     * When a packet is received, it parses the message and sends it to the Kafka topic using the Kafka producer.
     */
    public void start() {
        new Thread(() -> {
            try (DatagramSocket localSocket = socket != null ? socket : new DatagramSocket(port)) {
                running = true;
                byte[] buffer = new byte[1024];
                log.info("📡 Listening for {} on UDP port {}", type, port);
                while (running) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    localSocket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    log.debug("Received: {}", received);

                    SensorMessage msg = parseMessage(received);
                    if (msg != null) {
                        kafkaProducer.sendSensorMessage(msg);
                    } else {
                        log.warn("Invalid message: {}", received);
                    }
                }
            } catch (Exception e) {
                log.error("Error on UDP listener for {}: {}", type, e.getMessage(), e);
            }
        }).start();
    }

    /*
     * Stops the UDP listener by closing the socket and setting the running flag to false.
     */
    public void stop() {
        this.running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    /*
     * Parses the incoming message to extract the sensor ID and value.
     * The expected format is "sensorId=value".
     * @param input The incoming message as a string.
     * @return A SensorMessage object containing the parsed sensor ID, value, and type.
     */
    public SensorMessage parseMessage(String input) {
        try {
            String[] parts = input.split(";");
            String sensorId = parts[0].split("=")[1];
            int value = Integer.parseInt(parts[1].split("=")[1]);
            return new SensorMessage(sensorId, value, type);
        } catch (Exception e) {
            log.warn("❗ Invalid message received: '{}'. Error: {}", input, e.getMessage());
            return null;
        }
    }

}
