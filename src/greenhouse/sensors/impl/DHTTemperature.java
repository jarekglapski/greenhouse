package greenhouse.sensors.impl;

import java.io.IOException;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

public class DHTTemperature implements Sensor {

    private final DHTSensor sensor;

    public DHTTemperature(DHTSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String getID() {
        return "DHT@" + sensor.getID() + "-temp";
    }

    @Override
    public Number getValue() throws IOException {
        return sensor.getTemperature();
    }

    @Override
    public PhysicalQuantity getPhysicalQuantity() {
        return PhysicalQuantity.Temperature;
    }

    @Override
    public String getUnitString() {
        return "*C";
    }
}
