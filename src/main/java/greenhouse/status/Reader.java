package greenhouse.status;

import greenhouse.sensors.Sensor;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public interface Reader {

    static Measurement getMeasurement(Sensor sensor) {
        try {
            return new Measurement(new Date(), sensor.getID(), sensor.getValue(), sensor.getUnitString(), sensor.getPhysicalQuantity());
        } catch (IOException ex) {
            Logger.getLogger(SimpleReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Set<Measurement> getLastMeasurement();

    public Set<Sensor> getSensors();

    public void setSensors(Set<Sensor> sensors);
}
