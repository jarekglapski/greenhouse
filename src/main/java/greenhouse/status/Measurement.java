package greenhouse.status;

import greenhouse.sensors.PhysicalQuantity;
import greenhouse.sensors.Sensor;

import java.util.Date;

/**
 *
 */
public class Measurement {

    private Date date;
    private String sensorId;
    private Number value;
    private PhysicalQuantity physicalQuantity;

    public Measurement(Date date, String id, Number value, PhysicalQuantity physicalQuantity) {
        this.date = date;
        this.sensorId = id;
        this.value = value;
        this.physicalQuantity = physicalQuantity;
    }

    public Measurement(Date date, Sensor sensor) {
        this.date = date;
        this.sensorId = sensor.getID();
        this.value = sensor.getValue();
        this.physicalQuantity = sensor.getPhysicalQuantity();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public PhysicalQuantity getPhysicalQuantity() {
        return physicalQuantity;
    }

    public String getUnit() {
        return physicalQuantity.getUnit();
    }

    public void setPhysicalQuantity(PhysicalQuantity physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }
    
    @Override
    public String toString() {
        return String.format("%1$s(%2$s) | %3$tF %3$tT | %4$3.2f%5$s", getPhysicalQuantity(), getSensorId(), getDate(), getValue().floatValue(), getUnit());
    }
}
