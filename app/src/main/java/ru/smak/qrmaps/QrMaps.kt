package ru.smak.qrmaps

import android.app.Application
import com.yandex.mapkit.MapKitFactory

object QrMaps : Application() {
    init{
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
    }
}