import com.denisballa.warehouse.config.ConfigLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ConfigLoaderTests {

    @Test
    void testLoadProperties() {
        String kafkaBroker = ConfigLoader.get("kafka.bootstrap.servers");
        assertNotNull(kafkaBroker);
        assertEquals("localhost:9092", kafkaBroker);
        String kafkaTopic = ConfigLoader.get("kafka.topic");
        assertNotNull(kafkaTopic);
        assertEquals("sensor-data", kafkaTopic);
        int sensorTemperatureUdpPort = ConfigLoader.getInt("sensor.temperature.port");
        assertEquals(3344, sensorTemperatureUdpPort);
        int sensorHumidityUdpPort = ConfigLoader.getInt("sensor.humidity.port");
        assertEquals(3355, sensorHumidityUdpPort);
    }

}
