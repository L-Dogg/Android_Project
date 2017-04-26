package pl.kuc_industries.warsawnavihelper.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.DateFormat;
import java.util.ArrayList;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pl.kuc_industries.warsawnavihelper.Constants;
import pl.kuc_industries.warsawnavihelper.FetchAddressIntentService;
import pl.kuc_industries.warsawnavihelper.Models.TramAndBusLine;
import pl.kuc_industries.warsawnavihelper.R;
import pl.kuc_industries.warsawnavihelper.DrawerItems.TramAndBusGridAdapter;
import pl.kuc_industries.warsawnavihelper.ZTM.MapUtils.VehicleItem;
import pl.kuc_industries.warsawnavihelper.ZTM.MapUtils.VehicleType;
import pl.kuc_industries.warsawnavihelper.ZTM.Provider.ZTM2MapProvider;
import pl.kuc_industries.warsawnavihelper.ZTM.Provider.ZTM2ViewProvider;
import pl.kuc_industries.warsawnavihelper.adapter.CustomExpandableListAdapter;

import static pl.kuc_industries.warsawnavihelper.Constants.TRAM_AND_BUS_LINES_PER_ROW;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {
    protected static final String TAG = "MainActivity";

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    protected GoogleApiClient mGoogleApiClient;
    protected GoogleMap mGoogleMap;
    private ClusterManager<VehicleItem> mClusterManager;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected String mAddressOutput;
    private AddressResultReceiver mResultReceiver;

    protected Boolean mRequestingLocationUpdates;
    protected Boolean mAddressRequested;
    protected String mLastUpdateTime;

    private List<TramAndBusLine> mTramAndBusLines;

    private Drawer result = null;
    private ZTM2ViewProvider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResultReceiver = new AddressResultReceiver(new Handler());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_drawer_header)
                .build();

        mTramAndBusLines = getTramAndBusLineList();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new ExpandableDrawerItem().withName("Tram and Bus").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("Trams").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        Context context = view.getContext();
                                        new MaterialDialog.Builder(view.getContext()).
                                                title("Select your tram lines").
                                                adapter(new TramAndBusGridAdapter(context, mTramAndBusLines,
                                                                                    VehicleType.Tram, mProvider),
                                                        new GridLayoutManager(context, TRAM_AND_BUS_LINES_PER_ROW)).
                                                show();
                                        return true;
                                    }
                                }).withIcon(GoogleMaterial.Icon.gmd_collection_bookmark).withIdentifier(2002),
                                new SecondaryDrawerItem().withName("Buses").withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                                    @Override
                                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                        Context context = view.getContext();
                                        new MaterialDialog.Builder(view.getContext()).
                                                title("Select your tram lines").
                                                adapter(new TramAndBusGridAdapter(context, mTramAndBusLines,
                                                                                    VehicleType.Bus, mProvider),
                                                        new GridLayoutManager(context, TRAM_AND_BUS_LINES_PER_ROW)).
                                                show();
                                        return true;
                                    }
                                }).withIcon(GoogleMaterial.Icon.gmd_collection_bookmark).withIdentifier(2003)
                        ),
                        new PrimaryDrawerItem().withName("Veturilo").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(19).withSelectable(false),
                        new ExpandableDrawerItem().withName("ATM").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2005),
                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2006)
                        ),
                        new ExpandableDrawerItem().withName("Air Pollution").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2005),
                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2006)
                        )
                )
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                //.withShowDrawerUntilDraggedOpened(true)
                .build();

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
        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        Log.wtf(TAG, "onCreate() before new fragment");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startLocationUpdates();
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
            updateUI();
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

    public void findTramOrBusButtonHandler(View view) {
        Log.wtf(TAG, "findTramOrBusButtonHandler invoking provider");
        //mProvider.getBuses(208);
        mProvider.getTrams(9);
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
                updateUI();
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

        mAddressRequested = true;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        startIntentService();
        updateUI();
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
        mProvider = new ZTM2MapProvider(mClusterManager);
    }

    private void updateUI() {
        if(mCurrentLocation == null || mGoogleMap == null)
            return;
    }

    public void centerMapOnCurrentLocation()
    {
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
            updateUI();
        }
    }

    private List<TramAndBusLine> getTramAndBusLineList() {
        List<TramAndBusLine> tramAndBusLineList = new ArrayList<>();
        String[] tramAndBusLineNames = getResources().getStringArray(R.array.tram_lines);

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

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<VehicleItem>(this, mGoogleMap);
        mClusterManager.setRenderer(new VehicleItemRenderer(getApplicationContext(), mGoogleMap, mClusterManager));
        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
    }

    public class VehicleItemRenderer  extends DefaultClusterRenderer<VehicleItem> {
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
            // Draw a single person.
            // Set the info window to show their name.
           int resId = vehicle.getVehicleType() == VehicleType.Bus ? R.drawable.temporary_bus_splash :
                                                                    R.drawable.temporary_tram_splash;

            mImageView.setImageResource(resId);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(vehicle.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<VehicleItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
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
}
