package pl.kuc_industries.warsawnavihelper.APIs.ATM.Provider;

import java.util.List;

import pl.kuc_industries.warsawnavihelper.APIs.ATM.QueryResult.ATM;

public interface IATM2ControllerProvider {
    void ShowOnMap(List<ATM> atmList);
}
