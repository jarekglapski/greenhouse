package greenhouse.sensors;

public enum PhysicalQuantity {

    Temperature("*C"),
    Humidity("%");

    private final String unit;

    private PhysicalQuantity(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
