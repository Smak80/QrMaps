package ru.smak.qrmaps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.smak.qrmaps.R
import ru.smak.qrmaps.ui.theme.QrMapsTheme

@Composable
fun QrPage(
    image: ImageBitmap?,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        image?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(48.dp)
            )
        } ?: run {
            Icon(
                painter = painterResource(id = R.drawable.twotone_image_not_supported_48),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(48.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
fun QrPagePreview(){
    QrMapsTheme {
        QrPage(image = null, modifier = Modifier.fillMaxSize())
    }
}