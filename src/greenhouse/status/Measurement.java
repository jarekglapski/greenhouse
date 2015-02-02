package greenhouse.status;

import greenhouse.sensors.PhysicalQuantity;
import java.util.Date;

/**
 *
 */
public class Measurement {

    private Date date;
    private String sensorId;
    private Number value;
    private String unit;
    private PhysicalQuantity physicalQuantity;

    Measurement(Date date, String id, Number value, String unitString, PhysicalQuantity physicalQuantity) {
        this.date = date;
        this.sensorId = id;
        this.value = value;
        this.unit = unitString;
        this. physicalQuantity = physicalQuantity;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public PhysicalQuantity getPhysicalQuantity() {
        return physicalQuantity;
    }

    public void setPhysicalQuantity(PhysicalQuantity physicalQuantity) {
        this.physicalQuantity = physicalQuantity;
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s):%3.2f%s", getPhysicalQuantity(), getSensorId(), getValue(), getUnit());
    }
}
