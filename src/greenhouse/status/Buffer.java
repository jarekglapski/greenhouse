package greenhouse.status;

import greenhouse.sensors.Sensor;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 *
 */
public class Buffer extends TimerTask implements Reader {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private static final int QUEUE_SIZE = 250;

    private final Reader sensorsReader;

    CircularFifoQueue<Set<Measurement>> queue = new CircularFifoQueue<>(QUEUE_SIZE);

    public Buffer(Reader sensorsReader) {
        this.sensorsReader = sensorsReader;
    }

    @Override
    public void run() {
        Set<Measurement> measurements = sensorsReader.getLastMeasurement();
        if (null != measurements) {
            try {
                writeLock.lock();
                queue.add(sensorsReader.getLastMeasurement());
            } finally {
                writeLock.unlock();
            }
        }
    }

    @Override
    public Set<Measurement> getLastMeasurement() {
        try {
            readLock.lock();
            return queue.peek();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Set<Sensor> getSensors() {
        return sensorsReader.getSensors();
    }

    @Override
    public void setSensors(Set<Sensor> sensors) {
        sensorsReader.setSensors(sensors);
    }

}
