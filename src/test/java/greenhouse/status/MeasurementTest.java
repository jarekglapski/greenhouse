package greenhouse.status;

import greenhouse.sensors.PhysicalQuantity;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;


public class MeasurementTest {

    @Test
    public void testHumidityMeasurementToString() throws Exception {
        String value = new Measurement(new Date(), "testID", 20, PhysicalQuantity.Humidity).toString();
        assertThat("Measurement string value for humidity contains correct physical quantity and sensor id", value, startsWith("Humidity(testID) | "));
        assertThat("Measurement string value for humidity contains value and unit", value, endsWith(" | 20.00%"));

    }

    @Test
    public void testTempMeasurementToString() throws Exception {
        String value = new Measurement(new Date(), "123", -12.65, PhysicalQuantity.Temperature).toString();
        assertThat("Measurement string value for temperature contains correct physical quantity and sensor id", value, startsWith("Temperature(123) | "));
        assertThat("Measurement string value for temperature contains value and unit", value, endsWith(" | -12.65*C"));
    }
}