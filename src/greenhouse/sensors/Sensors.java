package greenhouse.sensors;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import greenhouse.sensors.impl.DHTType;
import greenhouse.sensors.impl.DallasSensorDS18B20;
import greenhouse.sensors.impl.DHTHygrometer;
import greenhouse.sensors.impl.DHTSensor;
import greenhouse.sensors.impl.DHTTemperature;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class Sensors {

    private static final String SESNORS_FOLDER_PATH = "/sys/bus/w1/devices";
    private static final String SENSOR_FILE_PREFIX = "w1_bus_master";

    private static final Logger LOG = Logger.getLogger(Sensors.class.getName());

    /**
     * @return all available sensors.
     */
    public static Set<Sensor> getSensors() {
        Set<Sensor> sensors = new HashSet<>();
        addDallasSensors(sensors);
        addDHTSensors(sensors);
        return sensors;
    }

    private static void addDHTSensors(Set<Sensor> sensors) {
        Properties props = new Properties();
        try {
            props.load(Sensors.class.getResourceAsStream("/dhtsensors.properties"));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not find sensors properties!", e);
            return;
        }
        for (int i = 0; true; i++) {
            String typeStr = props.getProperty(String.format("sensor%d.type", i));
            if (typeStr == null) {
                break;
            }
            DHTType type = DHTType.getType(typeStr);
            if (type != null) {
                String pin = props.getProperty(String.format("sensor%d.pin", i));
                if (pin == null) {
                    continue;
                }
                DHTSensor sensor = new DHTSensor(type, Integer.parseInt(pin));
                sensors.add(new DHTTemperature(sensor));
                sensors.add(new DHTHygrometer(sensor));
            } else {
                LOG.log(Level.WARNING, "Sensor type {0} not supported.", type);
            }
        }
    }

    private static void addDallasSensors(Set<Sensor> sensors) {
        File sensorFolder = new File(SESNORS_FOLDER_PATH);
        if (!sensorFolder.exists()) {
            LOG.log(Level.SEVERE, "Could not find w1 devices! Please ensure that mods w1-gpio and w1-therm are loaded.");
            return;
        }
        for (File f : sensorFolder.listFiles()) {
            if (f.getName().startsWith(SENSOR_FILE_PREFIX)) {
                continue;
            }
            sensors.add(new DallasSensorDS18B20(f));
        }
    }
}
