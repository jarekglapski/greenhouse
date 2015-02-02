package greenhouse.status;

import java.util.TimerTask;

/**
 *
 */
public class Logger extends TimerTask {

    private final Reader sensorsReader;

    public Logger(Reader sensorsReader) {
        this.sensorsReader = sensorsReader;
    }

    @Override
    public void run() {
        sensorsReader.getLastMeasurement().stream().forEach((measurement) -> {
            java.util.logging.Logger.getLogger(this.getClass().getName()).info(measurement.toString());
        });
    }
}
