package greenhouse.status;

import java.util.TimerTask;

/**
 *
 */
public class Logger extends TimerTask {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Logger.class.getName());

    private final Reader sensorsReader;

    public Logger(Reader sensorsReader) {
        this.sensorsReader = sensorsReader;
    }

    @Override
    public void run() {
        sensorsReader.getLastMeasurement().stream().forEach((measurement) -> {
            LOG.info(measurement.toString());
        });
    }
}
