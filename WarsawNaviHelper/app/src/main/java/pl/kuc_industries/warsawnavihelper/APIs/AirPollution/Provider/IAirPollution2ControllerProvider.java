package pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult.AirPollution;

public interface IAirPollution2ControllerProvider {
    /*
    This method should modify drawer's "Air Pollution" item according to data from rest api
     */
    void ModifyInterface(AirPollution airPollution);
}
