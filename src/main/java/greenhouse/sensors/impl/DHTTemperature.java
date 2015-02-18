package greenhouse.sensors.impl;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

public class DHTTemperature implements Sensor {

    private final DHTSensor sensor;

    public DHTTemperature(DHTSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String getID() {
        return String.format("%s-temp", sensor.getID());
    }

    @Override
    public Number getValue() {
        return sensor.getTemperature();
    }

    @Override
    public PhysicalQuantity getPhysicalQuantity() {
        return PhysicalQuantity.Temperature;
    }

    @Override
    public String getUnitString() {
        return PhysicalQuantity.Temperature.getUnit();
    }
}
