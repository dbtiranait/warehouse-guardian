import com.denisballa.warehouse.model.SensorMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SensorMessageTests {

    @Test
    void testAllArgsConstructorAndGetters() {
        SensorMessage msg = new SensorMessage("s1", 42, "temperature");
        assertEquals("s1", msg.getSensorId());
        assertEquals(42, msg.getValue());
        assertEquals("temperature", msg.getType());
    }

    @Test
    void testSettersAndToString() {
        SensorMessage msg = new SensorMessage();
        msg.setSensorId("s2");
        msg.setValue(30);
        msg.setType("humidity");
        assertTrue(msg.toString().contains("s2"));
        assertTrue(msg.toString().toLowerCase().contains("humidity"));
    }

    @Test
    void testEqualsAndHashCode() {
        SensorMessage a = new SensorMessage("s1", 50, "temperature");
        SensorMessage b = new SensorMessage("s1", 50, "temperature");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

}