package ru.smak.qrmaps.ui

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.smak.qrmaps.R
import ru.smak.qrmaps.ui.theme.QrMapsTheme

@Composable
fun QrButton(
    onClick: ()->Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.twotone_qr_code_2_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun LoadButton(
    onClick: ()->Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_download_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview
@Composable
fun QrButtonPreview(){
    QrMapsTheme {
        QrButton(onClick = {  })
    }
}

@Preview
@Composable
fun LoadButtonPreview(){
    QrMapsTheme {
        LoadButton(onClick = {  })
    }
}