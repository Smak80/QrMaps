package ru.smak.qrmaps.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun TrackPage(
    path: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier,
) {
    var offset: Offset by remember { mutableStateOf(Offset.Zero) }
    var size: Offset by remember { mutableStateOf(Offset.Zero) }
    Column(modifier = modifier) {
        YaMap(
            offset,
            size,
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned {
                    offset = it.positionInRoot()
                    size = if (it.size == IntSize.Zero)
                        Offset(1f, 1f)
                    else
                        Offset(it.size.width.toFloat(), it.size.height.toFloat())
                },
            path,
        )
    }
}

@Composable
fun YaMap(
    topLeftOffset: Offset,
    bottomRightOffset: Offset,
    modifier: Modifier = Modifier,
    path: List<Pair<Double, Double>> = listOf(),
){
    val density = LocalDensity.current.run{ 1.dp.toPx() }
    val pathPoint = path.map { Point(it.first, it.second) }
    Box(modifier = modifier) {
        AndroidView(
            factory = {
                MapView(it).apply {
                    if (path.isNotEmpty()) {
                        mapWindow.map.mapObjects.addCircle(Circle(pathPoint.last(), 5f))
                    }
                }
            },
            update = { mapView ->
                mapView.setFocusRect(topLeftOffset, bottomRightOffset, 32*density)
                mapView.createPath(pathPoint, (16*density).toInt())
            })
    }
}

private fun MapView.createPath(
    path: List<Point>,
    pointRadius: Int,
) {
    if (path.isNotEmpty()) {
        val polyline = Polyline(path)
        val geometry = Geometry.fromPolyline(polyline)
        mapWindow.map.cameraPosition(geometry).let {
            mapWindow.map.move(it)
        }
        mapWindow.map.mapObjects.addPolyline(polyline).apply {
            strokeWidth = 6.5f
            setStrokeColor(Color.GREEN)
            outlineWidth = 1.5f
            outlineColor = Color.MAGENTA
        }
        val img = ImageProvider.fromBitmap(createBitmap(pointRadius))
        mapWindow.map.mapObjects.addCollection().apply {
            path.forEach {point ->
                addPlacemark().also {
                    it.geometry = point
                    it.setIcon(img)
                }
            }
        }
    }
}

fun createBitmap(pointRadius: Int): Bitmap {
    val bmp = Bitmap.createBitmap(pointRadius, pointRadius, Bitmap.Config.ARGB_8888)
    val fp = Paint().apply { color = Color.GREEN }
    val sp = Paint().apply { color = Color.MAGENTA }
    val c = Canvas(bmp)
    c.drawCircle(pointRadius/2f, pointRadius/2f, pointRadius/2f, sp)
    c.drawCircle(pointRadius/2f, pointRadius/2f, pointRadius/2f-4f, fp)
    return bmp
}

private fun MapView.setFocusRect(
    topLeftOffset: Offset,
    bottomRightOffset: Offset,
    padding: Float,
) {
    val tlx = topLeftOffset.x + padding
    val tly = topLeftOffset.y + padding
    val brx = (bottomRightOffset.x - padding).let{ if (it < tlx) tlx + 1 else it}
    val bry = (bottomRightOffset.y - padding).let{ if (it < tly) tly + 1 else it}
    mapWindow.focusRect = ScreenRect(
        ScreenPoint(tlx, tly),
        ScreenPoint(brx, bry)
    )
}
