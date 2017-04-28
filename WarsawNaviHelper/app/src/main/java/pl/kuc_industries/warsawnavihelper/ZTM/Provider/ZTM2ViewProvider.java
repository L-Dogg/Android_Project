package pl.kuc_industries.warsawnavihelper.ZTM.Provider;

public interface ZTM2ViewProvider {
    public void getTrams(String lineNumber);
    public void getBuses(String lineNumber);
    public void removeVehiclesFromMap(String lineNumber);
}
