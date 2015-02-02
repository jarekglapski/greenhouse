package greenhouse.status;

import greenhouse.sensors.Sensor;
import java.util.Set;
import java.util.TimerTask;

/**
 *
 */
public class SimpleReader extends TimerTask implements Reader {

    private Set<Sensor> sensors;

    private Set<Measurement> lastMeasurement;

    public SimpleReader(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public void run() {
        lastMeasurement.clear();
        for (Sensor sensor : sensors) {
            store(Reader.getMeasurement(sensor));
        }
    }

    protected synchronized void store(Measurement measurement) {
        lastMeasurement.add(measurement);
    }

    @Override
    public synchronized Set<Measurement> getLastMeasurement() {
        return lastMeasurement;
    }

    @Override
    public Set<Sensor> getSensors() {
        return sensors;
    }

    @Override
    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }
}
