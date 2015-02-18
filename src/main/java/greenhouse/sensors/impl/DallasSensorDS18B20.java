package greenhouse.sensors.impl;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DS18B20 type temperature sensor.
 */
public class DallasSensorDS18B20 implements Sensor {

    private static final Logger LOG = Logger.getLogger(DallasSensorDS18B20.class.getName());

    private static final String VALUE_FILE_NAME = "w1_slave";
    private static final String TEMP_VALUE_PREFIX = "t=";

    private final File sensorFile;
    private final File valueFile;

    public DallasSensorDS18B20(File sensorFile) {
        this.sensorFile = sensorFile;
        this.valueFile = deriveValueFile(sensorFile);
    }

    @Override
    public String getID() {
        return String.format("DS18B20@%s", sensorFile.getName());
    }

    @Override
    public Number getValue() {
        float value = Float.NEGATIVE_INFINITY;
        try (BufferedReader reader = new BufferedReader(new FileReader(valueFile))) {
            int index;
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                index = line.indexOf(TEMP_VALUE_PREFIX);
                if (index >= 0) {
                    value = Integer.parseInt(line.substring(index + 2)) / 1000f;
                    break;
                }
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, String.format("Could not read sensor [%s]", getID()), ioe);
        }
        return value;
    }

    private static File deriveValueFile(File sensorFile) {
        return new File(sensorFile, VALUE_FILE_NAME);
    }

    @Override
    public String toString() {
        return String.format("Sensor ID: %s, Temperature: %2.3f%s", getID(), getValue().floatValue(), getUnitString());
    }

    @Override
    public PhysicalQuantity getPhysicalQuantity() {
        return PhysicalQuantity.Temperature;
    }

    @Override
    public String getUnitString() {
        return PhysicalQuantity.Temperature.getUnit();
    }
}
