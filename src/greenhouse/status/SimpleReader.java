package greenhouse.status;

import greenhouse.sensors.Sensor;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

/**
 *
 */
public class SimpleReader extends TimerTask implements Reader {

    private Set<Sensor> sensors;

    private final Set<Measurement> lastMeasurement = new HashSet<>();

    public SimpleReader(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public synchronized void run() {
        lastMeasurement.clear();
        for (Sensor sensor : sensors) {
            store(Reader.getMeasurement(sensor));
        }
    }

    protected void store(Measurement measurement) {
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
