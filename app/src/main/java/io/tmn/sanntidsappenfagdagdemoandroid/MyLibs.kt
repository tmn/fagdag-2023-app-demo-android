package io.tmn.sanntidsappenfagdagdemoandroid

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import io.tmn.sanntidsappenfagdagdemoandroid.models.Feature

class MyLibs {
    companion object {
        val instance: MyLibs by lazy {
            MyLibs()
        }
    }

    fun distanceToCurrentLocation(feature: Feature, currentLocation: LatLng): Double {
        if (feature == null || currentLocation == null) {
            return 0.0
        }
        return getDistance(
            start_lat = feature.geometry.coordinates[1],
            start_lon = feature.geometry.coordinates[0],
            end_lat = currentLocation.latitude,
            end_lon = currentLocation.longitude)
    }

    external fun stringFromJNI(): String
    external fun getDistance(start_lat: Double, start_lon: Double, end_lat: Double, end_lon: Double): Double
}