package com.example.testcompany.daf.vue;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;

import java.lang.ref.WeakReference;

class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {

    private final WeakReference<MapsActivity> activityWeakReference;

    LocationListeningCallback(MapsActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }


    @Override
    public void onSuccess(LocationEngineResult result) {
        MapsActivity activity = activityWeakReference.get();

        if (activity != null) {
            Location location = result.getLastLocation();

            if (location == null) {
                return;
            }
/*
// Create a Toast which displays the new location's coordinates
            Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
                    String.valueOf(result.getLastLocation().getLatitude()), String.valueOf(result.getLastLocation().getLongitude())),
                    Toast.LENGTH_SHORT).show();
*/
// Pass the new location to the Maps SDK's LocationComponent
            if (activity.mapboxMap != null && result.getLastLocation() != null) {
                activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        Log.d("LocationChangeActivity", exception.getLocalizedMessage());
        MapsActivity activity = activityWeakReference.get();
        if (activity != null) {
            Toast.makeText(activity, exception.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
