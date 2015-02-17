package greenhouse.sensors.impl;

import greenhouse.sensors.PhysicalQuantity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class DHTTemperatureTest {

    private DHTTemperature sensor;

    @Before
    public void setUp() throws Exception {
        sensor = new DHTTemperature(new DHTSensor(DHTType.DHT11, 4));
    }

    @After
    public void tearDown() throws Exception {
        sensor = null;
    }

    @Test
    public void testGetID() throws Exception {
        assertEquals("DHT11@4-temp", sensor.getID());
    }

    @Test
    public void testGetValue() throws Exception {
        DHTSensor dhtSensor = new DHTSensor(DHTType.DHT11,4);
        DHTSensor dhtSensorSpy = spy(dhtSensor);
        sensor = new DHTTemperature(dhtSensorSpy);
        when(dhtSensorSpy.readValues()).thenReturn("Humidity=10.5% Temp=-3.4*");
        assertEquals(-3.4, sensor.getValue().floatValue(),0.001);
        verify(dhtSensorSpy).readValues();
    }

    @Test
    public void testGetPhysicalQuantity() throws Exception {
        assertEquals(PhysicalQuantity.Temperature, sensor.getPhysicalQuantity());
    }

    @Test
    public void testGetUnitString() throws Exception {
        assertEquals("*C", sensor.getUnitString());
    }
}