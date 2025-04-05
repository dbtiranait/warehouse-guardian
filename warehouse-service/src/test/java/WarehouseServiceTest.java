import com.denisballa.warehouse.WarehouseService;
import org.junit.jupiter.api.Test;

public class WarehouseServiceTest {

    @Test
    void testMainRunsWithoutCrash() {
        WarehouseService.main(new String[]{});
    }

}