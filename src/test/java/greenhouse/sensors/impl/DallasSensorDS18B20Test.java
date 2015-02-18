package greenhouse.sensors.impl;

import greenhouse.sensors.PhysicalQuantity;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class DallasSensorDS18B20Test {
    private static File file;

    static {
        URL url = Thread.currentThread().getContextClassLoader().getResource("28-0214635566ff");
        file = new File(url.getPath());
    }

    @Test
    public void testGetID() throws Exception {
        assertThat("ID is readable", new DallasSensorDS18B20(file).getID(), equalTo("DS18B20@28-0214635566ff"));
    }

    @Test
    public void testGetValue() throws Exception {
        DallasSensorDS18B20 sensor = new DallasSensorDS18B20(file);
        assertThat("Temperature read from file", sensor.getValue(), equalTo(17.625f));
    }


    @Test
    public void testGetPhysicalQuantity() throws Exception {
        DallasSensorDS18B20 sensor = new DallasSensorDS18B20(file);
        assertThat("PhysicalQuantity set to temperature", sensor.getPhysicalQuantity(), is(PhysicalQuantity.Temperature));
    }

    @Test
    public void testGetUnitString() throws Exception {
        DallasSensorDS18B20 sensor = new DallasSensorDS18B20(file);
        assertThat("Unit set to Celsius degrees", sensor.getUnitString(), is(PhysicalQuantity.Temperature.getUnit()));
    }
}