package ru.smak.qrmaps.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.smak.qrmaps.ui.navigation.Navigation

class MainViewModel(app: Application) : AndroidViewModel(app) {
    var currentPage: Navigation by mutableStateOf(Navigation.LIST)

    private val qrScanner by lazy{
        GmsBarcodeScanning.getClient(app.applicationContext)
    }

    fun loadQr(onLoad: (List<Pair<Double, Double>>)->Unit) {
        qrScanner.startScan()
            .addOnSuccessListener {
                val result = it.rawValue?.split("\n")?.map { pair ->
                    pair.split(";", limit = 2).let{ latLon->
                        try {
                            latLon[0].toDouble() to latLon[1].toDouble()
                        } catch (_: Throwable) {
                            null
                        }
                    }
                }?.filterNotNull()
                onLoad(result ?: listOf())
            }
            .addOnFailureListener{
                onLoad(listOf())
            }
    }

}