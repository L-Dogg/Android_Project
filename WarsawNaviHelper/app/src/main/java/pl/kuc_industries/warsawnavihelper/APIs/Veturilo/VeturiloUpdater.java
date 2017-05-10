package pl.kuc_industries.warsawnavihelper.APIs.Veturilo;

import android.util.Log;

import java.util.TimerTask;

import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider.IVeturilo2ViewProvider;

/**
 * Created by mateusz on 5/10/17.
 */

public class VeturiloUpdater extends TimerTask {

    private IVeturilo2ViewProvider viewProvider;

    public VeturiloUpdater(IVeturilo2ViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override
    public void run() {
        viewProvider.updateStationsOnView();
        Log.wtf("VeturiloUpdater", "Update triggered!");
    }
}
