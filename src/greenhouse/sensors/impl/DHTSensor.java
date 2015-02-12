package greenhouse.sensors.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class representing a DHT sensor. Uses the Adafruit_DHT driver, which
 * must be copied to somewhere on the path.
 *
 */
public class DHTSensor {

    private static final Logger LOG = Logger.getLogger(DHTSensor.class.getName());
    private static final String ADAFRUIT_PROCESS_TEMPLATE = "sudo AdafruitDHT %s %d";
    private final static String TEMP_STR = "Temp=";
    private final static String HUM_STR = "Humidity=";
    private final int gpioPin;
    private final DHTType type;
    private String lastValue;
    private long lastCheck;

    public DHTSensor(DHTType type, int gpioPin) {
        this.type = type;
        this.gpioPin = gpioPin;
    }

    public String getID() {
        return String.valueOf(gpioPin);
    }

    public synchronized float getHumidity() {
        checkForUpdates();
        return parseHumidity(lastValue);
    }

    private void checkForUpdates() {
        long now = System.currentTimeMillis();
        if (now - lastCheck > 3000) {
            String newValues = readValues();
            if (newValues.indexOf('%') > 0) {
                lastValue = newValues;
                lastCheck = now;
            }
        }
    }

    public synchronized float getTemperature() {
        checkForUpdates();
        return parseTemperature(lastValue);
    }

    private float parseTemperature(String value) {
        if (value == null) {
            return Float.MIN_VALUE;
        }
        return Float.parseFloat(value.substring(value.indexOf(TEMP_STR)
                + TEMP_STR.length(), value.indexOf('*')));
    }

    private float parseHumidity(String value) {
        if (value == null) {
            return Float.MIN_VALUE;
        }
        return Float.parseFloat(value.substring(value.indexOf(HUM_STR)
                + HUM_STR.length(), value.indexOf('%')));
    }

    private String readValues() {
        String result = "";
        try {
            Process p = Runtime.getRuntime().exec(String.format(ADAFRUIT_PROCESS_TEMPLATE, type.getCode(), gpioPin));
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, String.format("Could not read the %s sensor at pin %d", type.getCode(), gpioPin), e);
            return null;
        }
        return result;
    }

}
