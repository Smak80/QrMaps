package ru.smak.qrmaps.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.smak.qrmaps.database.LocationDb
import ru.smak.qrmaps.database.Track
import ru.smak.qrmaps.locating.Locator
import ru.smak.qrmaps.permissions.LocationPermissionRegister
import ru.smak.qrmaps.qrcode.QrCreator

class TrackMapViewModel(app: Application) : AndroidViewModel(app) {

    val path: SnapshotStateList<Point> = SnapshotStateList()

    private var activeTrackId: Long? = null

    private val locationPermissions: LocationPermissionRegister = LocationPermissionRegister()

    private var updJob: Job? = null

    private val fusedLocationClient = LocationServices
        .getFusedLocationProviderClient(app.applicationContext)

    /**
     * Метод должен быть вызван в OnCreate() активности
     * @param activity Активность, в которой будут запрашиываться разрешения
     */
    fun registerPermissionRequester(activity: ComponentActivity){
        locationPermissions.registerInActivity(activity){ permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                }
                else -> {
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            }
        }
        locationPermissions.launchIfNeeded(activity)
    }

    private fun createQr() {
        if (path.isNotEmpty()) {
            path.joinToString(
                separator = "\n"
            ) { "${it.latitude};${it.longitude}" }.let {
                Log.d("QR", it)
                viewModelScope.launch {
                    QrCreator().run {
                        val bmp = create(it)
                        saveToFile(getApplication<Application>().applicationContext, bmp)
                    }
                }
            }
        }
    }

    private var _trackingOn by mutableStateOf(false)

    val trackingOn: Boolean
        get() = _trackingOn

    fun changeTrackingState(context: Context) {
        _trackingOn = !_trackingOn
        if (_trackingOn) startLocationUpdates(context)
        else stopLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(context: Context) {
        if (locationPermissions.isLocationPermissionsGranted(context)) {
            fusedLocationClient.lastLocation.addOnCompleteListener {
                viewModelScope.launch {
                    activeTrackId = LocationDb.createNewTrack()
                    fusedLocationClient.requestLocationUpdates(
                        Locator.locationRequest,
                        Locator.locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
            updJob = viewModelScope.launch {
                Locator.location.collect {
                    onUpdateLocation(it)
                }
            }
        }
    }

    private fun onUpdateLocation(location: Location?) {
        location?.let{
            activeTrackId?.let { trackId ->
                viewModelScope.launch {
                    LocationDb.addPoint(trackId, it)
                }
            }
            path.add(Point(it.latitude, it.longitude))
        }
    }

    private fun stopLocationUpdates() {
        updJob?.cancel()
        activeTrackId = null
        fusedLocationClient.removeLocationUpdates(Locator.locationCallback)
        createQr()
    }
}