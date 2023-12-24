package ru.smak.qrmaps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yandex.mapkit.MapKitFactory
import ru.smak.qrmaps.ui.ListPage
import ru.smak.qrmaps.ui.theme.QrMapsTheme
import ru.smak.qrmaps.ui.TrackPage
import ru.smak.qrmaps.ui.navigation.Navigation
import ru.smak.qrmaps.viewmodels.MainViewModel
import ru.smak.qrmaps.viewmodels.TrackListViewModel
import ru.smak.qrmaps.viewmodels.TrackMapViewModel

class MainActivity : ComponentActivity() {

    private val tmvm: TrackMapViewModel by viewModels()
    private val tlvm: TrackListViewModel by viewModels()
    private val mvm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tmvm.registerPermissionRequester(this)

        setContent {
            QrMapsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUi(
                        Modifier.fillMaxSize(),
                        trackingOn = tmvm.trackingOn,
                        onTrackingClick = {
                            tmvm.changeTrackingState(this)
                            mvm.currentPage = if (tmvm.trackingOn)
                                Navigation.TRACK
                            else
                                Navigation.LIST
                        }
                    ){
                        when (mvm.currentPage){
                            Navigation.LIST -> {
                                ListPage(
                                    tlvm.tracks.collectAsState(initial = listOf()).value,
                                    modifier = Modifier.fillMaxSize(),
                                ) {

                                }
                            }
                            Navigation.TRACK -> {
                                TrackPage(
                                    tmvm.path,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUi(
    modifier: Modifier = Modifier,
    trackingOn: Boolean = false,
    onTrackingClick: ()->Unit = {},
    content: @Composable ()->Unit,
){
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                },
                colors = TopAppBarDefaults
                    .centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
            )
        },
        bottomBar = {},
        snackbarHost = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = onTrackingClick
            ) {
                Icon(
                    painter = painterResource(id = if (trackingOn) R.drawable.baseline_location_disabled_24 else R.drawable.baseline_my_location_24),
                    contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview(locale = "ru")
@Composable
fun MainUiPreview(){
    QrMapsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainUi(
                Modifier.fillMaxSize(),
            ){
            }
        }
    }
}