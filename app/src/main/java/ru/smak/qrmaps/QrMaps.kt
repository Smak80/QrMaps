package ru.smak.qrmaps

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.smak.qrmaps.database.LocationDb

class QrMaps : Application() {

    override fun onCreate() {
        super.onCreate()
        LocationDb.init(applicationContext)
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(applicationContext)
    }

}