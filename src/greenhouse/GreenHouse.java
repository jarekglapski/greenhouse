/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse;

import greenhouse.sensors.Sensor;
import greenhouse.sensors.Sensors;
import greenhouse.status.SimpleReader;
import greenhouse.status.remote.BasicGoogleSpreadsheetPoster;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Logger;

/**
 *
 * @author jaroslaw_glapski
 */
public class GreenHouse {

    private static final Logger LOG = Logger.getLogger(GreenHouse.class.getName());
    
    private static final long READING_DELAY = 1000l;
    private static final long READING_INTERVAL = 1000l;
    private static final long LOGGING_DELAY = 2500l;
    private static final long LOGGING_INTERVAL = 1000l;
    private static final long GOOGLE_LOGGING_DELAY = 2600l;
    private static final long GOOGLE_LOGGING_INTERVAL = 30 * 1000l;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Set<Sensor> sensors = Sensors.getSensors();
        LOG.info(String.format("%d sensors found", sensors.size()));

        Timer timer = new Timer();
        
        SimpleReader sensorsReader = new SimpleReader(sensors);
        
        timer.schedule(sensorsReader, READING_DELAY, READING_INTERVAL);
        timer.schedule(new greenhouse.status.Logger(sensorsReader), LOGGING_DELAY, LOGGING_INTERVAL);
        //timer.schedule(new BasicGoogleSpreadsheetPoster(null), GOOGLE_LOGGING_DELAY, GOOGLE_LOGGING_INTERVAL);
    }

}
