package ru.smak.qrmaps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.smak.qrmaps.R
import ru.smak.qrmaps.database.ExtendedTrackInfo
import ru.smak.qrmaps.ui.theme.QrMapsTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun ListPage(
    tracks: List<ExtendedTrackInfo>,
    modifier: Modifier = Modifier,
    onSelectTrack: (ExtendedTrackInfo)->Unit = {},
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tracks){
                TrackCard(
                    it,
                    modifier = Modifier.fillMaxWidth(),
                    onQrClick = {

                    },
                    onClick = onSelectTrack
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackCard(
    trackInfo: ExtendedTrackInfo,
    modifier: Modifier = Modifier,
    onQrClick: (ExtendedTrackInfo)->Unit = {},
    onClick: (ExtendedTrackInfo)->Unit = {},
) {
    ElevatedCard(
        onClick = { onClick(trackInfo) },
        modifier = modifier
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = stringResource(R.string.track_num_lbl),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
                Text(text = trackInfo.id.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.twotone_map_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.start),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = trackInfo.started?.format(
                                DateTimeFormatter.ofLocalizedDateTime(
                                    FormatStyle.SHORT
                                )
                            )
                                ?: stringResource(id = R.string.no_time),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.finish),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = trackInfo.finished?.format(
                                DateTimeFormatter.ofLocalizedDateTime(
                                    FormatStyle.SHORT
                                )
                            )
                                ?: stringResource(R.string.no_time),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                FilledIconButton(
                    onClick = { onQrClick(trackInfo) },
                    modifier = Modifier.minimumInteractiveComponentSize(),
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
        }
    }
}

@Preview(locale = "ru")
@Composable
fun MainPagePreview(){
    val tracks = listOf(
        ExtendedTrackInfo(1, true, LocalDateTime.now(), LocalDateTime.now()),
        ExtendedTrackInfo(2, false, LocalDateTime.now(), LocalDateTime.now()),
        ExtendedTrackInfo(3, true, LocalDateTime.now(), LocalDateTime.now()),
    ).reversed()
    QrMapsTheme {
        ListPage(
            tracks,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(locale = "ru")
@Composable
fun TrackCardPreview(){
    QrMapsTheme {
        TrackCard(
            trackInfo = ExtendedTrackInfo(
                333,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )
    }
}