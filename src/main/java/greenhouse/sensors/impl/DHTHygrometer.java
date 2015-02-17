package greenhouse.sensors.impl;

import java.io.IOException;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

public class DHTHygrometer implements Sensor {

    private final DHTSensor sensor;

    public DHTHygrometer(DHTSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String getID() {
        return "DHT@" + sensor.getID() + "-hygro";
    }

    @Override
    public Number getValue() throws IOException {
        return sensor.getHumidity();
    }

    @Override
    public PhysicalQuantity getPhysicalQuantity() {
        return PhysicalQuantity.Humidity;
    }

    @Override
    public String getUnitString() {
        return "%";
    }
}
