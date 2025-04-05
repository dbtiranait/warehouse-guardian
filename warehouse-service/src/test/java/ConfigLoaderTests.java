import com.denisballa.warehouse.config.ConfigLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTests {

    @Test
    void testLoadExistingProperties() {
        assertEquals("localhost:9092", ConfigLoader.get("kafka.bootstrap.servers"));
        assertEquals("sensor-data", ConfigLoader.get("kafka.topic"));
        assertEquals(3344, ConfigLoader.getInt("sensor.temperature.port"));
        assertEquals(3355, ConfigLoader.getInt("sensor.humidity.port"));
    }

    @Test
    void testMissingPropertyReturnsNull() {
        assertNull(ConfigLoader.get("non.existent.key"));
    }

    @Test
    void testMissingIntReturnsZero() {
        assertEquals(0, ConfigLoader.getInt("non.existent.int.key"));
    }

    @Test
    void testInvalidIntFormatReturnsZero() {
        assertEquals(0, ConfigLoader.getInt("kafka.bootstrap.servers")); // not an int
    }

}
