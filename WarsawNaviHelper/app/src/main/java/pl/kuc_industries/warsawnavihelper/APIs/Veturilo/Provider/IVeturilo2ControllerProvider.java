package pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider;

import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.QueryResult.VeturiloQueryResult;

/**
 * Created by mateusz on 5/6/17.
 */

public interface IVeturilo2ControllerProvider {
    void showOnMap(VeturiloQueryResult veturiloStations);
}
