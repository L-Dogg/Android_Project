package pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.AirPollutionController;
import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult.AirPollution;
import pl.kuc_industries.warsawnavihelper.Constants;

import static android.widget.Toast.makeText;

public class AirPollutionProvider implements IAirPollution2ControllerProvider, IAirPollution2ViewProvider{
    private final static String TAG = "AirPollutionProvider";

    private final AirPollutionController mAirPollutionController;
    private final Context mContext;

    //TODO: passing Context so we can see Toast with information
    //We can change this after there's an indicator in the drawer
    public AirPollutionProvider(Context ctx) {
        mAirPollutionController = new AirPollutionController(this);
        mContext = ctx;
        mAirPollutionController.start();
    }

    @Override
    public void UpdateAirPollutionData() {
        mAirPollutionController.getAirPollutionData();
    }

    @Override
    public void ModifyInterface(AirPollution airPollution) {
        String levelName = airPollution.getStIndexLevel().getIndexLevelName();
        Log.wtf(TAG,  "Air quality: " + levelName);
        Toast t = Toast.makeText(mContext, "Air quality: " + Constants.polishToEnglishDescrptionsMap.get(levelName), Toast.LENGTH_LONG);
        t.show();
    }
}
