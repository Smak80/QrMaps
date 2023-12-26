package ru.smak.qrmaps

import android.app.Application
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.yandex.mapkit.MapKitFactory
import ru.smak.qrmaps.database.LocationDb

class QrMaps : Application() {

    private val moduleInstallClient by lazy {
        ModuleInstall.getClient(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        LocationDb.init(applicationContext)
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(applicationContext)

        val qrScanner = GmsBarcodeScanning.getClient(applicationContext)
//        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
//            .addApi(qrScanner)
//            .build()

        moduleInstallClient
            .areModulesAvailable(qrScanner)
            .addOnSuccessListener {
                if (!it.areModulesAvailable()) {
                    moduleInstallClient
                        .deferredInstall(qrScanner)
                    //    .installModules(moduleInstallRequest)
                }
            }
    }

}