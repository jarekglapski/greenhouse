package greenhouse.sensors.impl;

/**
 * The supported types of DHT sensors.
 *
 */
public enum DHTType {

    AM2302("2302"), DHT11("11"), DHT22("22");

    private final String dhtCode;

    DHTType(String dhtCode) {
        this.dhtCode = dhtCode;
    }

    public String getCode() {
        return dhtCode;
    }

    public static DHTType getType(String dhtCode) {
        for (DHTType dhtType : values()) {
            if (dhtType.getCode().equals(dhtCode)) {
                return dhtType;
            }
        }
        return null;
    }
}
