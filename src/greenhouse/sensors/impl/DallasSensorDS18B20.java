package greenhouse.sensors.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

/**
 * DS18B20 type temperature sensor.
 *
 *
 */
public class DallasSensorDS18B20 implements Sensor {

    private static final String VALUE_FILE_NAME = "w1_slave";
    private static final String CELC_DEG_STRING = "\u00B0C";
    private static final String TEMP_VALUE_PREFIX = "t=";

    private final File sensorFile;
    private final File valueFile;

    public DallasSensorDS18B20(File sensorFile) {
        this.sensorFile = sensorFile;
        this.valueFile = deriveValueFile(sensorFile);
    }

    @Override
    public String getID() {
        return sensorFile.getName();
    }

    @Override
    public Number getValue() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(
                valueFile))) {
            String tmp = reader.readLine();
            int index = -1;
            while (tmp != null) {
                index = tmp.indexOf(TEMP_VALUE_PREFIX);
                if (index >= 0) {
                    break;
                }
                tmp = reader.readLine();
            }
            if (index < 0) {
                throw new IOException("Could not read sensor " + getID());
            }
            return Integer.parseInt(tmp.substring(index + 2)) / 1000f;
        }
    }

    private static File deriveValueFile(File sensorFile) {
        return new File(sensorFile, VALUE_FILE_NAME);
    }

    @Override
    public String toString() {
        try {
            return String.format("Sensor ID: %s, Temperature: %2.3f%s", getID(), getValue(), getUnitString());
        } catch (IOException e) {
            return String.format("Sensor ID: %s, Could not read temperature!", getID());
        }
    }

    @Override
    public PhysicalQuantity getPhysicalQuantity() {
        return PhysicalQuantity.Temperature;
    }

    @Override
    public String getUnitString() {
        return CELC_DEG_STRING;
    }
}
