package greenhouse.sensors.impl;

import greenhouse.sensors.PhysicalQuantity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DHTHygrometerTest {

    private DHTHygrometer sensor;

    @Before
    public void setUp() throws Exception {
        sensor = new DHTHygrometer(new DHTSensor(DHTType.DHT11, 4));
    }

    @After
    public void tearDown() throws Exception {
        sensor = null;
    }

    @Test
    public void testGetID() throws Exception {
        assertEquals("DHT11@4-hygro", sensor.getID());
    }

    @Test
    public void testGetValue() throws Exception {
        DHTSensor dhtSensor = new DHTSensor(DHTType.DHT11,4);
        DHTSensor dhtSensorSpy = spy(dhtSensor);
        sensor = new DHTHygrometer(dhtSensorSpy);
        when(dhtSensorSpy.readValues()).thenReturn("Humidity=10.5% Temp=-3.4*");
        assertEquals(10.5, sensor.getValue().floatValue(),0.001);
        verify(dhtSensorSpy).readValues();
    }

    @Test
    public void testGetPhysicalQuantity() throws Exception {
        assertEquals(PhysicalQuantity.Humidity, sensor.getPhysicalQuantity());
    }

    @Test
    public void testGetUnitString() throws Exception {
        assertEquals("%", sensor.getUnitString());
    }
}