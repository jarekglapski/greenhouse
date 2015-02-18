package greenhouse.status;

import greenhouse.sensors.Sensor;

import java.util.Date;
import java.util.Set;

/**
 *
 */
public interface Reader {

    static Measurement getMeasurement(Sensor sensor) {
        return new Measurement(new Date(), sensor);
    }

    public Set<Measurement> getLastMeasurement();

    public Set<Sensor> getSensors();

    public void setSensors(Set<Sensor> sensors);
}
