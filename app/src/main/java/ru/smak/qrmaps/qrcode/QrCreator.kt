package ru.smak.qrmaps.qrcode

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.set
import io.nayuki.qrcodegen.QrCode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.ZoneOffset

object QrCreator {
    fun create(data: String): Bitmap{
        val qr = QrCode.encodeText(data, QrCode.Ecc.MEDIUM)
        return toImage(qr, 5, 5)
    }

    fun saveToFile(context: Context, image: Bitmap) {
        val bos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 0, bos)
        File(context.cacheDir, "qr-${LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)}.png").also { file ->
            file.createNewFile()
            FileOutputStream(file).apply {
                write(bos.toByteArray())
                flush()
                close()
            }
        }
    }

    private fun toImage(
        qr: QrCode,
        scale: Int,
        border: Int,
        lightColor: Int = Color.White.toArgb(),
        darkColor: Int = Color.Black.toArgb(),
    ): Bitmap {
        require(!(scale <= 0 || border < 0)) { "Value out of range" }
        require(!(border > Int.MAX_VALUE / 2 || qr.size + border * 2L > Int.MAX_VALUE / scale)) { "Scale or border too large" }
        val result = Bitmap.createBitmap(
            (qr.size + border * 2) * scale,
            (qr.size + border * 2) * scale,
            Bitmap.Config.ARGB_8888,
        )
        for (y in 0 ..< result.height) {
            for (x in 0 ..< result.width) {
                val color = qr.getModule(x / scale - border, y / scale - border)
                result[x, y] = if (color) darkColor else lightColor
            }
        }
        return result
    }
}