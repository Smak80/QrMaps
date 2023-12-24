package ru.smak.qrmaps.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import ru.smak.qrmaps.database.ExtendedTrackInfo
import ru.smak.qrmaps.database.LocationDb

class TrackListViewModel(app: Application) : AndroidViewModel(app) {

    val tracks: Flow<List<ExtendedTrackInfo>>
        get() = LocationDb.getXTracksFlow()

}