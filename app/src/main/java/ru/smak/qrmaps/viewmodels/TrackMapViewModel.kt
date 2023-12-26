package ru.smak.qrmaps.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.smak.qrmaps.database.LocationDb
import ru.smak.qrmaps.database.LocationDb.asString
import ru.smak.qrmaps.locating.Locator
import ru.smak.qrmaps.permissions.LocationPermissionRegister
import ru.smak.qrmaps.qrcode.QrCreator

class TrackMapViewModel(app: Application) : AndroidViewModel(app) {

    val trackPoints: SnapshotStateList<Pair<Double, Double>> = SnapshotStateList()

    var activeTrackId: Long? = null

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

    fun createQr(trackId: Long? = activeTrackId, onImageReady: (Bitmap?)->Unit = {}) {
        viewModelScope.launch {
            onImageReady(trackId?.let {
                val track = LocationDb.getPointsForTrack(it).asString()
                QrCreator.create(track)
            })
        }
    }

    private fun saveQr(qr: Bitmap){
        viewModelScope.launch {
            QrCreator.saveToFile(getApplication<Application>().applicationContext, qr)
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
                    trackPoints.clear()
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
            trackPoints.add(it.latitude to it.longitude)
        }
    }

    private fun stopLocationUpdates() {
        updJob?.cancel()
        fusedLocationClient.removeLocationUpdates(Locator.locationCallback)
        activeTrackId?.let{ trackId ->
            createQr(trackId){ result ->
                result?.let{ bmp ->
                    saveQr(bmp)
                }
            }
        }
        activeTrackId = null
    }

    fun createTrackFromQr(points: List<Pair<Double, Double>>){
        if (points.isNotEmpty()) {
            viewModelScope.launch {
                activeTrackId = LocationDb.createNewTrack(false)
                activeTrackId?.let { trackId ->
                    trackPoints.apply {
                        clear()
                        addAll(points)
                        LocationDb.saveAllPointsToTrack(trackId, points)
                    }
                }
            }
        }
    }

    fun loadTrack(trackId: Long) {
        activeTrackId = trackId
        viewModelScope.launch {
            trackPoints.apply {
                clear()
                addAll(LocationDb.getPointsForTrack(trackId))
            }
        }
    }
}