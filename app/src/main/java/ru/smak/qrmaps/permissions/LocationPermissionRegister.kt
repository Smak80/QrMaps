package ru.smak.qrmaps.permissions

import android.Manifest
import android.content.Context

class LocationPermissionRegister : PermissionRegister(){

    fun isLocationPermissionsGranted(context: Context) = isPermissionsGranted(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        context = context)


    fun launchIfNeeded(context: Context){
        if (!isLocationPermissionsGranted(context)) {
            permissionRequester.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}