package com.example.testcompany.daf.vue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testcompany.daf.controleur.Controle;
import com.example.testcompany.daf.modele.Localisation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.example.testcompany.daf.R;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.geojson.exception.GeoJsonException;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.exception.NavigationException;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/*
@ReportsCrashes(formKey = "", // will not be used
        mailTo = "bekhechiyacine@hotmail.com",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text) */
/**
 * Draw a polyline by parsing a GeoJSON file with the Mapbox Android SDK.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {

    private final Controle controle;
    private MapView mapView;
    public MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationComponent locationComponent;
    private final LocationListeningCallback callback = new LocationListeningCallback(this);
    private final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private static final String TAG = "MapsActivity";
    private static final String SATELITE = Style.SATELLITE;
    private static final String MAPBOX_STREET = Style.MAPBOX_STREETS;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private Button button;
    private ImageView btnRecenter;
    private ImageView btnSecteur;
    private Location location;
    private Location locationSecteur;
    private boolean test = false;

    public MapsActivity() {
        this.controle = Controle.getInstance(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_maps);
        btnRecenter = findViewById(R.id.btnRecenter);
        btnSecteur = findViewById(R.id.btnSecteur);
        FloatingActionButton back = findViewById(R.id.backBtn);
        FloatingActionButton mapBtn = findViewById(R.id.mapBtn);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        ecouteBtnBack(back);
        ecouteBtnMap(mapBtn);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

            MapsActivity.this.mapboxMap = mapboxMap;
            loadStyle(MAPBOX_STREET);

    }

    private void loadStyle(String STYLE_TYPE){

        mapboxMap.setStyle(STYLE_TYPE, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                addDestinationIconSymbolLayer(style);

                mapboxMap.addOnMapClickListener(MapsActivity.this);
                button = findViewById(R.id.btnNavigation);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean simulateRoute = false;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();
                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(MapsActivity.this, options);
                    }
                });
                genererSecteur(style);
                addMarker(style);
                ecouteBtnRecenter();
                ecouteBtnSecteur();
            }
        });

    }

    private void genererSecteur(@NonNull Style style){

        final ArrayList<Localisation> secteur = controle.retourneSecteur();
        final List<List<Point>> leSecteur = remplirPointsSecteur(secteur);

        if(secteur.size() > 0){
            locationSecteur = new Location("not defined");
            Double longitude = secteur.get(0).getLongitude();
            Double latitude = secteur.get(0).getLatitude();
            locationSecteur.setLongitude(longitude);
            locationSecteur.setLatitude(latitude);
            setCameraPositionSecteur(locationSecteur);
        }else{
            Toast.makeText(this, "Votre secteur n'a pas été attribué, Veuillez réessayer !", Toast.LENGTH_SHORT).show();
        }

        style.addSource(new GeoJsonSource("source-id", Polygon.fromLngLats(leSecteur)));

       /* style.addLayer(new LineLayer("layer-id", "source-id")
                .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                        PropertyFactory.lineOpacity(.7f),
                        PropertyFactory.lineWidth(7f),
                        PropertyFactory.lineColor(Color.parseColor("#04102E")))); */

                    style.addLayerBelow(new FillLayer("layer-id", "source-id")
                            .withProperties(
                            fillColor(Color.parseColor("#310942EC"))), "settlement-label"
                    );
    }

    private List<List<Point>> remplirPointsSecteur(ArrayList<Localisation> secteur){

        final List<List<Point>> POINTS = new ArrayList<>();
        final List<Point> OUTER_POINTS = new ArrayList<>();

        for (int i=0;i<secteur.size();i++){

            OUTER_POINTS.add(Point.fromLngLat(secteur.get(i).getLongitude(), secteur.get(i).getLatitude()));
        }
        POINTS.add(OUTER_POINTS);
        return POINTS;
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        if (controle.haveNetwork()){
            Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
            Point originPoint = null;
            if (locationComponent.getLastKnownLocation() != null) {
                originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                        locationComponent.getLastKnownLocation().getLatitude());
            }

            GeoJsonSource source = Objects.requireNonNull(mapboxMap.getStyle()).getSourceAs("destination-source-id");
            if (source != null) {
                source.setGeoJson(Feature.fromGeometry(destinationPoint));
            }

            //if (originPoint != null && destinationPoint != null){

                getRoute(originPoint, destinationPoint);
                button.setEnabled(true);
                button.setBackgroundResource(R.color.mapboxBlue);

            //}

            return true;
        }else{
            Toast.makeText(this, "Vous n'avez pas de connexion internet !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void getRoute(Point origin, Point destination) {

        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        try{
                        if (navigationMapRoute != null) {
                            navigationMapRoute.updateRouteArrowVisibilityTo(false);
                            navigationMapRoute.updateRouteVisibilityTo(false);
                        } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }

                            navigationMapRoute.addRoute(currentRoute);

                        }catch(NavigationException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

           initializeLocationEngine();
           initializeLocationComponent(loadedMapStyle);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine(){

        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());

    }

    @SuppressWarnings({"MissingPermission", "WrongConstant"})
    private void initializeLocationComponent(@NonNull Style loadedMapStyle){

        //HERE TO DISPLAY THE ICON BLUE !
        // Get an instance of the component
        locationComponent = mapboxMap.getLocationComponent();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(this, loadedMapStyle)
                        .useDefaultLocationEngine(false)
                        .build();

        // Activate the MapboxMap LocationComponent to show user location
        // Adding in LocationComponentOptions is also an optional parameter
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this,loadedMapStyle).build());
        locationComponent.setLocationComponentEnabled(true);

        //L'APPEL DOIT SE FAIRE APRES locationComponent.activateLocationComponent
        locationEngine.getLastLocation(callback);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);

        location = locationComponent.getLastKnownLocation();

        //setCameraPosition(location);

    }

    private void setCameraPosition(Location location){

        if (location != null){
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13.0));
        }
    }

    private void setCameraPositionSecteur(Location location){

        if (location != null){
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15.0));
        }
    }

    private void ecouteBtnRecenter(){
        btnRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location = locationComponent.getLastKnownLocation();
                setCameraPosition(location);
            }
        });
    }

    private void ecouteBtnSecteur(){
        btnSecteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCameraPositionSecteur(locationSecteur);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            if (isLocationEnabled()){
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
            }else{
                showAlert();
            }

        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.alert_dialog_sale, null);
        //alertDialog.setView(view);
        alertDialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }


    private void addMarker(@NonNull Style style) {
        ArrayList<Localisation> locations = controle.retourneLocationClients();
        List<Feature> markers = new ArrayList<>();
        try{
            for (int i=0;i<locations.size();i++) {

                markers.add(Feature.fromGeometry(
                        Point.fromLngLat(locations.get(i).getLongitude(), locations.get(i).getLatitude())));

            }

                // Add the marker image to map
                style.addImage("marker-icon-id",
                        BitmapFactory.decodeResource(
                                MapsActivity.this.getResources(), R.drawable.mapbox_marker_icon_default));


                GeoJsonSource geoJsonSource = new GeoJsonSource("SOURCE-ID", FeatureCollection.fromFeatures(markers));
                style.addSource(geoJsonSource);

                SymbolLayer symbolLayer = new SymbolLayer("layer-id-markers", "SOURCE-ID");
                symbolLayer.withProperties(
                        PropertyFactory.iconImage("marker-icon-id"),
                        iconAllowOverlap(true),
                        iconOffset(new Float[] {0f, -9f})
                );
                style.addLayer(symbolLayer);

        }catch(GeoJsonException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isLocationEnabled()) {
            mapView.onStart();
        }else{
            showAlert();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }

        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    private void ecouteBtnBack(FloatingActionButton back){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void ecouteBtnMap(FloatingActionButton mapBtn){
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!test){
                    //mapboxMap.setStyle(Style.SATELLITE);
                    loadStyle(SATELITE);
                    test = true;
                }else {
                    //mapboxMap.setStyle(Style.MAPBOX_STREETS);
                    loadStyle(MAPBOX_STREET);
                    test = false;
                }
            }
        });
    }
}