package pl.kuc_industries.warsawnavihelper.ZTM;

public enum ZTMVehicleType {
    Bus(1),
    Tram(2);

    private int value;
    private ZTMVehicleType(int value) { this.value = value; }
    public int getValue() { return value; }
}
