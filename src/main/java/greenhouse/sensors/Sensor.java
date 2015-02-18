package greenhouse.sensors;

/**
 * A simple interface for a sensor.
 *
 */
public interface Sensor {

    /**
     * @return the identity of the sensor. The format of the ID depends on the
     * kind of sensor. For example, Dallas protocol sensors will use their
     * unique device ID. 2302 devices will have two sensors per device, the id
     * consisting of the kind of sensor and the gpio-port used.
     */
    String getID();

    /**
     * The value of the sensor.
     *
     * @return the value of the sensor.
     */
    Number getValue();

    /**
     * @return the kind of physical quantity the value represents, for example
     * Temperature.
     *
     * @see PhysicalQuantity
     */
    PhysicalQuantity getPhysicalQuantity();

    /**
     * @return the unit string post-fix to use when rendering the value, for
     * example %.
     */
    String getUnitString();
}
