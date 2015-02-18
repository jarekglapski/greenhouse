package greenhouse.sensors.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class representing a DHT sensor. Uses the AdafruitDHT driver, which
 * must be copied to somewhere on the path.
 */
public class DHTSensor {

    private static final Logger LOG = Logger.getLogger(DHTSensor.class.getName());
    private static final String ADAFRUIT_PROCESS_TEMPLATE = "sudo AdafruitDHT %s %d";
    private static final String TEMP_STR = "Temp=";
    private static final String HUM_STR = "Humidity=";
    private static final int MAX_READ_FREQUENCY = 3000;
    private final int gpioPin;
    private final DHTType type;
    private long lastCheck;
    private float temperature = Float.NEGATIVE_INFINITY;
    private float humidity = Float.NEGATIVE_INFINITY;

    public DHTSensor(DHTType type, int gpioPin) {
        this.type = type;
        this.gpioPin = gpioPin;
    }

    public String getID() {
        return String.format("DHT%s@%d", type.getCode(), gpioPin);
    }

    public synchronized float getHumidity() {
        checkForUpdates();
        return humidity;
    }

    public synchronized float getTemperature() {
        checkForUpdates();
        return temperature;
    }

    protected void checkForUpdates() {
        long now = System.currentTimeMillis();
        if (now - lastCheck > MAX_READ_FREQUENCY) {
            String newValues = readValues();
            if (newValues != null && newValues.indexOf('%') > 0) {
                temperature = parseTemperature(newValues);
                humidity = parseHumidity(newValues);
                lastCheck = now;
            }
        }
    }

    protected static float parseTemperature(String input) {
        float value = Float.NEGATIVE_INFINITY;
        try {
            value = Float.parseFloat(input.substring(input.indexOf(TEMP_STR)
                    + TEMP_STR.length(), input.indexOf('*')));
        } catch (NumberFormatException | NullPointerException e) {
            LOG.log(Level.SEVERE, String.format("Could not parse temperature from input: [%s]", input), e);
        }
        return value;
    }

    protected static float parseHumidity(String input) {
        float value = Float.NEGATIVE_INFINITY;
        try {
            value = Float.parseFloat(input.substring(input.indexOf(HUM_STR)
                    + HUM_STR.length(), input.indexOf('%')));
        } catch (StringIndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
            LOG.log(Level.SEVERE, String.format("Could not parse humidity from input: [%s]", input), e);
        }
        return value;
    }

    protected String readValues() {
        StringBuffer result = new StringBuffer();
        Process adafruitProcess;
        try {
            adafruitProcess = Runtime.getRuntime().exec(String.format(ADAFRUIT_PROCESS_TEMPLATE, type.getCode(), gpioPin));
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, String.format("Could not execute process: [%s]", String.format(ADAFRUIT_PROCESS_TEMPLATE, type.getCode(), gpioPin)), ioe);
            return null;
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                adafruitProcess.getInputStream()))) {

            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, String.format("Could not read the %s sensor at pin %d", type.getCode(), gpioPin), e);
            return null;
        }
        return result.toString();
    }

}
