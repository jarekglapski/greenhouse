package greenhouse.sensors.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DHTSensorTest {

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetID() throws Exception {
        DHTSensor sensor = new DHTSensor(DHTType.DHT11,4);
        assertEquals("DHT11@4",sensor.getID());
    }

    @Test
    public void testGetHumidity() throws Exception {
        DHTSensor dhtSensor = new DHTSensor(DHTType.DHT11,4);
        DHTSensor dhtSensorSpy = spy(dhtSensor);
        when(dhtSensorSpy.readValues()).thenReturn("Humidity=10.5% Temp=10*");
        assertEquals(10.5, dhtSensorSpy.getHumidity(),0.001);
        assertEquals(10.5, dhtSensorSpy.getHumidity(),0.001);
        verify(dhtSensorSpy,times(2)).checkForUpdates();
        verify(dhtSensorSpy).readValues();
    }

    @Test
    public void testGetTemperature() throws Exception {
        DHTSensor dhtSensor = new DHTSensor(DHTType.DHT11,4);
        DHTSensor dhtSensorSpy = spy(dhtSensor);
        when(dhtSensorSpy.readValues()).thenReturn("Humidity=10.5% Temp=-44*");
        assertEquals(-44, dhtSensorSpy.getTemperature(),0.001);
        assertEquals(-44, dhtSensorSpy.getTemperature(),0.001);
        verify(dhtSensorSpy,times(2)).checkForUpdates();
        verify(dhtSensorSpy).readValues();
    }

    @Test
    public void testParseHumidityWhenNull() throws Exception {
        assertEquals(Float.NEGATIVE_INFINITY, DHTSensor.parseHumidity(null),0.001);
    }

    @Test
    public void testParseHumidityWhenEmptyString() throws Exception {
        assertEquals(Float.NEGATIVE_INFINITY, DHTSensor.parseHumidity(""),0.001);
    }

    @Test
    public void testParseHumidityWhenSinglePercentSign() throws Exception {
        assertEquals(Float.NEGATIVE_INFINITY, DHTSensor.parseHumidity("%"),0.001);
    }

    @Test
    public void testParseHumidityWhenInvalidInput() throws Exception {
        assertEquals(Float.NEGATIVE_INFINITY, DHTSensor.parseHumidity("Humidity="),0.001);
    }

    @Test
    public void testParseHumidityWhenEmptyValue() throws Exception {
        assertEquals(Float.NEGATIVE_INFINITY, DHTSensor.parseHumidity("Humidity=%"),0.001);
    }

    @Test
    public void testParseHumidityWhenPositiveIntegerValue() throws Exception {
        assertEquals(10, DHTSensor.parseHumidity("Humidity=10%"),0.001);
    }

    @Test
    public void testParseHumidityWhenNegativeIntegerValue() throws Exception {
        assertEquals(-1, DHTSensor.parseHumidity("Humidity=-1%"),0.001);
    }

    @Test
    public void testParseHumidityWhenZeroIntegerValue() throws Exception {
        assertEquals(0, DHTSensor.parseHumidity("Humidity=0%"),0.001);
    }

    @Test
    public void testParseHumidityWhenNegativeFloatValue() throws Exception {
        assertEquals(-12.23, DHTSensor.parseHumidity("Humidity=-12.23%"),0.001);
    }

    @Test
    public void testParseHumidityWhenPositiveFloatValue() throws Exception {
        assertEquals(1.999, DHTSensor.parseHumidity("Humidity=1.999%"),0.001);
    }
}