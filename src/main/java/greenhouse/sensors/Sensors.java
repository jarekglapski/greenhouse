package greenhouse.sensors;

import greenhouse.sensors.impl.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sensors {

    public static final String W1_SENSORS_FOLDER_PATH = "/sys/bus/w1/devices";
    public static final String W1_SENSOR_FILE_PREFIX = "w1_bus_master";
    public static final String DHT_SENSORS_PROPERTIES = "/dhtsensors.properties";

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
        Properties properties = new Properties();
        try {
            properties.load(Sensors.class.getResourceAsStream(DHT_SENSORS_PROPERTIES));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not find sensors properties!", e);
            return;
        }
        for (int i = 0; true; i++) {
            String typeString = properties.getProperty(String.format("sensor%d.type", i));
            if (typeString == null) {
                break;
            }
            DHTType type = DHTType.getType(typeString);
            if (type != null) {
                String pin = properties.getProperty(String.format("sensor%d.pin", i));
                if (pin == null) {
                    continue;
                }
                DHTSensor sensor = new DHTSensor(type, Integer.parseInt(pin));
                sensors.add(new DHTTemperature(sensor));
                sensors.add(new DHTHygrometer(sensor));
            } else {
                LOG.log(Level.WARNING, "Sensor type {0} not supported.", typeString);
            }
        }
    }

    private static void addDallasSensors(Set<Sensor> sensors) {
        File sensorFolder = new File(W1_SENSORS_FOLDER_PATH);
        if (!sensorFolder.exists()) {
            LOG.log(Level.SEVERE, "Could not find w1 devices! Please ensure that mods w1-gpio and w1-therm are loaded.");
            return;
        }
        for (File f : sensorFolder.listFiles()) {
            if (f.getName().startsWith(W1_SENSOR_FILE_PREFIX)) {
                continue;
            }
            sensors.add(new DallasSensorDS18B20(f));
        }
    }
}
