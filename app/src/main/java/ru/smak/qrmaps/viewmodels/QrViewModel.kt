package ru.smak.qrmaps.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.AndroidViewModel

class QrViewModel(app: Application) : AndroidViewModel(app) {

    var qrImage: ImageBitmap? by mutableStateOf(null)

}