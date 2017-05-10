package pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.AirPollutionController;
import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.QueryResult.AirPollution;
import pl.kuc_industries.warsawnavihelper.Constants;

import static android.widget.Toast.makeText;

public class AirPollutionProvider implements IAirPollution2ControllerProvider, IAirPollution2ViewProvider{
    private final static String TAG = "AirPollutionProvider";

    private final AirPollutionController mAirPollutionController;

    private final Context mContext;
    private final SecondaryDrawerItem mAirPollutionDrawerItem;
    private final Drawer mDrawer;

    //TODO: passing Context so we can see Toast with information
    //We can change this after there's an indicator in the drawer
    public AirPollutionProvider(Context ctx, SecondaryDrawerItem drawerItem, Drawer drawer) {
        mAirPollutionController = new AirPollutionController(this);
        mContext = ctx;
        mAirPollutionDrawerItem = drawerItem;
        mDrawer = drawer;
        mAirPollutionController.start();
    }

    @Override
    public void UpdateAirPollutionData() {
        mAirPollutionController.getAirPollutionData();
    }

    @Override
    public void ModifyInterface(AirPollution airPollution) {
        String levelName = airPollution.getStIndexLevel().getIndexLevelName();
        String badge = Constants.polishToEnglishDescrptionsMap.get(levelName);
        int colorInt = Constants.badgeTextToColorMap.get(badge);

        Log.wtf(TAG,  "Air quality: " + levelName);

        mAirPollutionDrawerItem.withBadge(badge).withBadgeStyle(
                new BadgeStyle().withColor(colorInt)
        );
        mDrawer.updateItem(mAirPollutionDrawerItem);

        Toast t = Toast.makeText(mContext, "Air quality: " + badge, Toast.LENGTH_LONG);
        t.show();
    }
}
