package pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider;

public interface IZTM2ViewProvider {
    void getTrams(String lineNumber);
    void getBuses(String lineNumber);
    void removeVehiclesFromMap(String lineNumber);
    void updateVehiclesPositionsOnMap();
    int getLinesVisibleOnMapCount();

    void showAll();
    void hideAll();

    boolean getVisibility();
}
