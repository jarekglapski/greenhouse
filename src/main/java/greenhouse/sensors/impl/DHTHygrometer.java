package greenhouse.sensors.impl;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

public class DHTHygrometer implements Sensor {

    private final DHTSensor sensor;

    public DHTHygrometer(DHTSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String getID() {
        return String.format("%s-hygro", sensor.getID());
    }

    @Override
    public Number getValue() {
        return sensor.getHumidity();
    }

    @Override
    public PhysicalQuantity getPhysicalQuantity() {
        return PhysicalQuantity.Humidity;
    }

    @Override
    public String getUnitString() {
        return PhysicalQuantity.Humidity.getUnit();
    }
}
