package com.denisballa.warehouse.kafka;

import com.denisballa.warehouse.model.SensorMessage;
import com.denisballa.warehouse.config.ConfigLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * KafkaSensorProducer is responsible for sending sensor messages to a Kafka topic.
 * It uses the KafkaProducer class to send messages in JSON format.
 * The producer is initialized with the necessary properties such as bootstrap servers and topic.
 * The ObjectMapper is used for JSON serialization of the SensorMessage objects.
 * The sendSensorMessage method takes a SensorMessage object, converts it to JSON, and sends it to the specified Kafka topic.
 * The close method is used to close the producer when it is no longer needed.
 */

@Slf4j
public class KafkaSensorProducer {

    /*
    * KafkaProducer is a thread-safe class that allows you to send messages to a Kafka topic.
     */
    private final KafkaProducer<String, String> producer;
    /*
    * ObjectMapper is used to convert Java objects to JSON format and vice versa.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /*
    * The topic to which the sensor messages will be sent.
     */
    private final String topic;

    /*
    * Constructor initializes the Kafka producer with the necessary properties.
    * It retrieves the Kafka bootstrap servers and topic from the configuration.
    * The producer is created with String serializers for both key and value.
    * The ObjectMapper is used for JSON serialization.
     */
    public KafkaSensorProducer() {
        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigLoader.get("kafka.bootstrap.servers"));
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.topic = ConfigLoader.get("kafka.topic");
        this.producer = new KafkaProducer<>(kafkaProps);
    }

    /*
     * Constructor for testing purposes.
     * It allows the use of a mock Kafka producer to simulate sending messages without actually connecting to a Kafka cluster.
     * This is useful for unit testing and integration testing.
     * @param mockProducer The mock Kafka producer to be used for testing.
     * @param topic The Kafka topic to which the sensor messages will be sent.
     * This constructor is used in the test class to create a mock producer.
     */
    public KafkaSensorProducer(KafkaProducer<String, String> mockProducer, String topic) {
        this.producer = mockProducer;
        this.topic = topic;
    }

    /*
    * Sends a SensorMessage to the Kafka topic.
    * The message is serialized to JSON format before sending.
    * @param message The SensorMessage to be sent.
     */
    public void sendSensorMessage(SensorMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            producer.send(new ProducerRecord<>(topic, message.getSensorId(), json));
            log.info("✅ Sent to Kafka [topic={}]: {}", topic, json);
        } catch (Exception e) {
            log.error("❌ Failed to send message to Kafka: {}", e.getMessage(), e);
        }
    }

    /*
    * Closes the Kafka producer.
     */
    public void close() {
        producer.close();
    }

}
