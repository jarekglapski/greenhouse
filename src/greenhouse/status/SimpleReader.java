package greenhouse.status;

import greenhouse.sensors.Sensor;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class SimpleReader extends TimerTask implements Reader {

    private Set<Sensor> sensors;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private final Set<Measurement> lastMeasurement = new HashSet<>();

    public SimpleReader(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public void run() {
        writeLock.lock();
        try {
            lastMeasurement.clear();
            for (Sensor sensor : sensors) {
                store(Reader.getMeasurement(sensor));
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected void store(Measurement measurement) {
        writeLock.lock();
        try {
            lastMeasurement.add(measurement);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Set<Measurement> getLastMeasurement() {
        readLock.lock();
        try {
            return lastMeasurement;
        } finally {
            readLock.unlock();
        }
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
