package pl.kuc_industries.warsawnavihelper.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryToggleDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.leakcanary.LeakCanary;

import org.json.JSONException;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.MapUtils.AtmItem;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.Provider.ATM2MapProvider;
import pl.kuc_industries.warsawnavihelper.APIs.ATM.Provider.IATM2ViewProvider;
import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider.AirPollutionProvider;
import pl.kuc_industries.warsawnavihelper.APIs.AirPollution.Provider.IAirPollution2ViewProvider;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.MapUtils.VeturiloItem;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.Provider.Veturilo2MapProvider;
import pl.kuc_industries.warsawnavihelper.APIs.Veturilo.VeturiloUpdater;
import pl.kuc_industries.warsawnavihelper.Constants;
import pl.kuc_industries.warsawnavihelper.FetchAddressIntentService;
import pl.kuc_industries.warsawnavihelper.Models.TramAndBusLine;
import pl.kuc_industries.warsawnavihelper.R;
import pl.kuc_industries.warsawnavihelper.DrawerItems.TramAndBusGridAdapter;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.MapUtils.VehicleItem;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.MapUtils.VehicleType;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider.ZTM2MapProvider;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.Provider.IZTM2ViewProvider;
import pl.kuc_industries.warsawnavihelper.APIs.ZTM.TramAndBusMapUpdater;
import pl.kuc_industries.warsawnavihelper.DrawerItems.ExpandableSwitchDrawerItem;

import static pl.kuc_industries.warsawnavihelper.Constants.BUS_LINES_PER_ROW;
import static pl.kuc_industries.warsawnavihelper.Constants.TRAM__LINES_PER_ROW;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {
    // region Fields
    protected static final String TAG = "MainActivity";

    private final static String[] mPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int INITIAL_REQUEST = 1337;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static final long ZTM_UPDATE_INTERVAL_IN_MILISECONDS = 5000;
    private static final long VETURILO_UPDATE_INTERVAL_IN_MILISECONDS = 30000;
    private Timer mVeturiloTimer;
    private Timer mZTMTimer;

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    protected GoogleApiClient mGoogleApiClient;
    protected GoogleMap mGoogleMap;
    private ClusterManager<VehicleItem> mZTMClusterManager;
    private ClusterManager<AtmItem> mATMClusterManager;
    private ClusterManager<VeturiloItem> mVeturiloClusterManager;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected String mAddressOutput;
    private AddressResultReceiver mResultReceiver;

    protected Boolean mRequestingLocationUpdates;
    protected Boolean mAddressRequested;
    protected String mLastUpdateTime;

    private List<TramAndBusLine> mTramLines;
    private List<TramAndBusLine> mBusLines;

    private Drawer result = null;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private IATM2ViewProvider mATMProvider;
    private IZTM2ViewProvider mZTMProvider;
    private Veturilo2MapProvider mVeturiloProvider;
    private IAirPollution2ViewProvider mAirPollutionProvider;

    private String mBankFilter = null;
    private String mDefaultBank;
    private String mDefaultStop;
    private PrimaryDrawerItem mAirPollutionDrawerItem;
    private int mATMRadius;
    private boolean mAreATMsFiltered = false;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeakCanary.install(getApplication());

        try {
            Constants.initializeStopToLocationMap(getApplicationContext());
            Constants.initializePolishToEnglishDescrptionMap(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mResultReceiver = new AddressResultReceiver(new Handler());

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mZTMTimer = new Timer("ZTMUpdater", true);
        mVeturiloTimer = new Timer("VeturiloUpdater", true);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.drawer_header)
                .build();

        mTramLines = getTramLines();
        mBusLines = getBusLines();

        //region Material Drawer build

        mAirPollutionDrawerItem = new PrimaryDrawerItem().withName("Air Pollution")
            .withBadge("NA").withIcon(GoogleMaterial.Icon.gmd_cloud)
            .withIdentifier(2137).withSelectable(false).withOnDrawerItemClickListener(
                new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.wtf(TAG, "Update air pollution data");
                        mAirPollutionProvider.UpdateAirPollutionData();
                        return true;
                    }
                }).withSetSelected(false);

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new ExpandableSwitchDrawerItem().withName("Tram and Bus").withIcon(GoogleMaterial.Icon.gmd_transform).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("Trams").withIcon(GoogleMaterial.Icon.gmd_directions_railway).withIdentifier(190).withSelectable(false).
                                        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                            @Override
                                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                                Context context = view.getContext();
                                                new MaterialDialog.Builder(context).
                                                        title("Select your tram lines").
                                                        adapter(new TramAndBusGridAdapter(context, mTramLines,
                                                                        VehicleType.Tram, mZTMProvider),
                                                                new GridLayoutManager(context, TRAM__LINES_PER_ROW)).
                                                        positiveText("OK").
                                                        show();
                                                return true;
                                            }
                                        }),
                                new SecondaryDrawerItem().withName("Buses").withIcon(GoogleMaterial.Icon.gmd_directions_bus).withIdentifier(191).withSelectable(true).
                                        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                            @Override
                                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                                Context context = view.getContext();
                                                new MaterialDialog.Builder(context).
                                                        title("Select your bus lines").
                                                        adapter(new TramAndBusGridAdapter(context, mBusLines,
                                                                        VehicleType.Bus, mZTMProvider),
                                                                new GridLayoutManager(context, BUS_LINES_PER_ROW)).
                                                        positiveText("OK").
                                                        show();
                                                return true;
                                            }
                                        }),
                                new SecondaryDrawerItem().withName("Stops").withIcon(GoogleMaterial.Icon.gmd_directions_walk).withIdentifier(192).withSelectable(true).
                                        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                            @Override
                                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                                Context context = view.getContext();
                                                new MaterialDialog.Builder(context).
                                                        title("Change default stop").
                                                        items(R.array.tram_and_bus_stops).
                                                        itemsCallback(new MaterialDialog.ListCallback() {
                                                            @Override
                                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                                                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.stopToLocationMap.get(text.toString())));
                                                                mGoogleMap.animateCamera(zoom);
                                                            }
                                                        }).
                                                        show();
                                                return true;
                                            }
                                        })
                        ).withSetSelected(false)
                        .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                Log.wtf(TAG, "ZTM onCheckedChanged, state = " + isChecked);
                                if (isChecked)
                                    mZTMProvider.showAll();
                                else
                                    mZTMProvider.hideAll();
                            }
                        }),
                        new ExpandableSwitchDrawerItem().withName("Veturilo").withIcon(GoogleMaterial.Icon.gmd_bike).withIdentifier(20).withSelectable(false).withSubItems(
                                new SecondarySwitchDrawerItem().withName("Show empty stations").withIcon(GoogleMaterial.Icon.gmd_block_alt).withIdentifier(200).withSelectable(false).
                                        withOnCheckedChangeListener(new OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                                mVeturiloProvider.setEmptyStationsVisible(isChecked);
                                            }
                                        })
                        ).withSetSelected(false).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                mVeturiloProvider.setStationsVisible(isChecked);
                            }
                        }),
                        new ExpandableSwitchDrawerItem().withName("ATM").withIcon(GoogleMaterial.Icon.gmd_local_atm).withIdentifier(21).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("Custom bank").withIcon(GoogleMaterial.Icon.gmd_balance_wallet).withIdentifier(210).withSelectable(false).
                                        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                            @Override
                                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                                Context context = view.getContext();
                                                new MaterialDialog.Builder(context).
                                                        title("Change bank setting").
                                                        items(R.array.bank_types).
                                                        itemsCallback(new MaterialDialog.ListCallback() {
                                                            @Override
                                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                                mBankFilter = text.toString();
                                                            }
                                                        }).
                                                        show();
                                                return true;
                                            }
                                        }),
                                new SecondarySwitchDrawerItem().withName("Show non free ATMs").withIcon(GoogleMaterial.Icon.gmd_money).withIdentifier(211).withSelectable(false).
                                        withOnCheckedChangeListener(new OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                                mATMProvider.removeATMsFromMap();
                                                if (isChecked) {
                                                    if (mBankFilter != null)
                                                        mATMProvider.getFilteredATMs(mCurrentLocation, mATMRadius, mBankFilter);
                                                    else
                                                        mATMProvider.getFilteredATMs(mCurrentLocation, 1500, mDefaultBank);
                                                }
                                                else
                                                    mATMProvider.getATMs(mCurrentLocation, 1500);
                                            }
                                        }),
                                new SecondaryDrawerItem().withName("Select radius detection").withIcon(GoogleMaterial.Icon.gmd_remote_control).withIdentifier(212).withSelectable(false).
                                        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                            @Override
                                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                                Context context = view.getContext();
                                                new MaterialDialog.Builder(context).
                                                        title("Change ATM Radius").
                                                        items(R.array.atm_radius_values).
                                                        itemsCallback(new MaterialDialog.ListCallback() {
                                                            @Override
                                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                                if (position > 0) {
                                                                    mATMRadius = Integer.parseInt(text.toString());
                                                                }
                                                            }
                                                        }).
                                                        show();
                                                return true;
                                            }
                                        })
                        ).withSetSelected(false).withOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    int radius = mATMRadius != 0 ? mATMRadius : 1500;
                                    if (mAreATMsFiltered)
                                        mATMProvider.getFilteredATMs(mCurrentLocation, radius, mBankFilter);
                                    else
                                        mATMProvider.getATMs(mCurrentLocation, radius);
                                }
                                else
                                    mATMProvider.removeATMsFromMap();
                            }
                        }),
                        mAirPollutionDrawerItem
                )
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();
        //endregion

        mAirPollutionProvider = new AirPollutionProvider(this, mAirPollutionDrawerItem, result);

        getSupportActionBar().hide();

        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);
        }

        mAddressRequested = true;
        mAddressOutput = "";
        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissions, INITIAL_REQUEST);
            }
        }
        updateValuesFromBundle(savedInstanceState);
        getPreferences();
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        getPreferences();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mDefaultBank = prefs.getString(getString(R.string.pref_default_bank), "BPH");
        mBankFilter = mDefaultBank;
        mDefaultStop = prefs.getString(getString(R.string.pref_default_stop), "Nowowiejska");
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                showToast(mAddressOutput);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    protected void startLocationUpdates() {
        LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                mLocationSettingsRequest
        ).setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, MainActivity.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                "location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Log.e(TAG, errorMessage);
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                }
            }
        });

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return(PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        else
            return true;
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        //updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            //updateUI();
        }
        if (mRequestingLocationUpdates) {
            Log.i(TAG, "in onConnected(), starting location updates");
            startLocationUpdates();
        }
        if (!Geocoder.isPresent()) {
            Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;

        }
        if (mAddressRequested) {
            startIntentService();
        }
        if (mCurrentLocation != null) {
            centerMapOnCurrentLocation();
        }
    }

    protected void startIntentService() {
        Log.wtf(TAG, "startIntentService() - begin");

        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mCurrentLocation);
        startService(intent);

        Log.wtf(TAG, "startIntentService() - end (after startService())");
    }

    @Override
    public void onLocationChanged(Location location) {
        Location prevState = mCurrentLocation;
        mCurrentLocation = location;
        // Center map on first obtained data:
        if (prevState == null)
            centerMapOnCurrentLocation();

        mVeturiloProvider.setLocation(mCurrentLocation);
        mAddressRequested = true;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        startIntentService();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        LatLng coords = (mCurrentLocation == null) ?
                new LatLng(52.232222, 21.008333) :
                new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
        mGoogleMap.animateCamera(zoom);
        setUpClusterer();

        mATMProvider = new ATM2MapProvider(mATMClusterManager);
        mZTMProvider = new ZTM2MapProvider(mZTMClusterManager);
        mVeturiloProvider = new Veturilo2MapProvider(mVeturiloClusterManager, mCurrentLocation);
        mZTMTimer.scheduleAtFixedRate(new TramAndBusMapUpdater(mZTMProvider),
                2 * ZTM_UPDATE_INTERVAL_IN_MILISECONDS,
                ZTM_UPDATE_INTERVAL_IN_MILISECONDS);
        mVeturiloTimer.scheduleAtFixedRate(new VeturiloUpdater(mVeturiloProvider),
                2 * VETURILO_UPDATE_INTERVAL_IN_MILISECONDS,
                VETURILO_UPDATE_INTERVAL_IN_MILISECONDS);
    }

    public void centerMapOnCurrentLocation() {
        LatLng coords =
                new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
        mGoogleMap.animateCamera(zoom);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            Log.wtf(TAG, "onReceiveResult(), mAddressOutput = " + mAddressOutput);
            mAddressRequested = false;
        }
    }

    private List<TramAndBusLine> getTramLines() {
        List<TramAndBusLine> tramAndBusLineList = new ArrayList<>();
        String[] tramAndBusLineNames = getResources().getStringArray(R.array.tram_lines);

        for (String tramAndBusLineName : tramAndBusLineNames) {
            tramAndBusLineList.add(new TramAndBusLine(tramAndBusLineName));
        }
        return tramAndBusLineList;
    }

    private List<TramAndBusLine> getBusLines() {
        List<TramAndBusLine> tramAndBusLineList = new ArrayList<>();
        String[] tramAndBusLineNames = getResources().getStringArray(R.array.bus_lines);

        for (String tramAndBusLineName : tramAndBusLineNames) {
            tramAndBusLineList.add(new TramAndBusLine(tramAndBusLineName));
        }
        return tramAndBusLineList;
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void setUpClusterer() {
        // Position the map.
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mZTMClusterManager = new ClusterManager<>(this, mGoogleMap);
        mZTMClusterManager.setRenderer(new VehicleItemRenderer(getApplicationContext(), mGoogleMap, mZTMClusterManager));
        mGoogleMap.setOnCameraIdleListener(mZTMClusterManager);
        mGoogleMap.setOnMarkerClickListener(mZTMClusterManager);

        mATMClusterManager = new ClusterManager<>(this, mGoogleMap);
        mATMClusterManager.setRenderer(new AtmItemRenderer(getApplicationContext(), mGoogleMap, mATMClusterManager));
        mGoogleMap.setOnCameraIdleListener(mATMClusterManager);
        mGoogleMap.setOnMarkerClickListener(mATMClusterManager);

        mVeturiloClusterManager = new ClusterManager<>(this, mGoogleMap);
        mVeturiloClusterManager.setRenderer(new VeturiloItemRenderer(getApplicationContext(), mGoogleMap, mVeturiloClusterManager));
        mGoogleMap.setOnCameraIdleListener(mVeturiloClusterManager);
        mGoogleMap.setOnMarkerClickListener(mVeturiloClusterManager);
    }

    public class VehicleItemRenderer extends DefaultClusterRenderer<VehicleItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public VehicleItemRenderer(Context context, GoogleMap map, ClusterManager<VehicleItem> clusterManager) {
            super(context, map, clusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.vehicle_marker, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_vehicle_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(VehicleItem vehicle, MarkerOptions markerOptions) {
            // Draw a single vehicle
            int resId = vehicle.getVehicleType() == VehicleType.Bus ? R.drawable.temporary_bus_splash :
                    R.drawable.temporary_tram_splash;

            mImageView.setImageResource(resId);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(vehicle.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<VehicleItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple vehicles
            if (cluster.getSize() == 0)
                return;

            Drawable d;
            if (cluster.getItems().iterator().next().getVehicleType() == VehicleType.Bus)
                d = cluster.getSize() == 1 ? getDrawable(R.drawable.temporary_bus_splash) :
                        getDrawable(R.drawable.temporary_bus_splash_cluster);
            else
                d = cluster.getSize() == 1 ? getDrawable(R.drawable.temporary_tram_splash) :
                        getDrawable(R.drawable.temporary_tram_splash_cluster);

            mClusterImageView.setImageDrawable(d);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    public class AtmItemRenderer extends DefaultClusterRenderer<AtmItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public AtmItemRenderer(Context context, GoogleMap map, ClusterManager<AtmItem> clusterManager) {
            super(context, map, clusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.vehicle_marker, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_vehicle_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(AtmItem atm, MarkerOptions markerOptions) {
            // Draw a single vehicle
            int resId = R.mipmap.atm;

            mImageView.setImageResource(resId);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(atm.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<AtmItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple vehicles
            if (cluster.getSize() == 0)
                return;

            Drawable d = getDrawable(R.mipmap.atm_cluster);

            mClusterImageView.setImageDrawable(d);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    public class VeturiloItemRenderer extends DefaultClusterRenderer<VeturiloItem> {

        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private int mDimension;

        public VeturiloItemRenderer(Context context, GoogleMap map, ClusterManager<VeturiloItem> clusterManager) {
            super(context, map, clusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.station_marker, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_vehicle_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(VeturiloItem item, MarkerOptions markerOptions) {
            mImageView.setImageResource(R.mipmap.veturilo);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.
                    fromBitmap(icon)).
                    title(item.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<VeturiloItem> cluster, MarkerOptions markerOptions) {
            if (cluster.getSize() == 0) {
                return;
            }

            Drawable clusterImage = getDrawable(R.mipmap.veturilo);

            mClusterImageView.setImageDrawable(clusterImage);
            Bitmap icon = mClusterIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<VeturiloItem> cluster) {
            return cluster.getSize() > 3;
        }
    }
}
