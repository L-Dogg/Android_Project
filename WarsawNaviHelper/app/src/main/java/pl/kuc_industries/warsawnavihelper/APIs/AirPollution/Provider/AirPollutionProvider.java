package pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.AirPollutionController;
import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult.AirPollution;

public class AirPollutionProvider implements IAirPollution2ControllerProvider, IAirPollution2ViewProvider{
    private final static String TAG = "AirPollutionProvider";

    private final AirPollutionController mAirPollutionController;

    public AirPollutionProvider() {
        //todo: args probably needed to update UI
        mAirPollutionController = new AirPollutionController(this);
        mAirPollutionController.start();
    }

    @Override
    public void UpdateAirPollutionData() {
        mAirPollutionController.getAirPollutionData();
    }

    @Override
    public void ModifyInterface(AirPollution airPollution) {
        Log.wtf(TAG,  "Air quality: " +
                airPollution.getStIndexLevel().getIndexLevelName());
    }
}
