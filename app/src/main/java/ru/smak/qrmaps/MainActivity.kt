package ru.smak.qrmaps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import ru.smak.qrmaps.ui.theme.QrMapsTheme
import ru.smak.qrmaps.ui.theme.YaMap
import ru.smak.qrmaps.ui.theme.navigation.Navigation
import ru.smak.qrmaps.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val mvm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        mvm.registerPermissionRequester(this)

        setContent {
            QrMapsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUi(
                        Modifier.fillMaxSize(),
                        trackingOn = mvm.trackingOn,
                        onTrackingClick = {
                            mvm.changeTrackingState(this)
                        }
                    ){
                        when (mvm.currentPage){
                            Navigation.MAIN -> { MainPage(mvm.path, modifier = Modifier.fillMaxSize()) }
                            Navigation.OTHER -> {  }
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

@Composable
fun MainPage(
    path: List<Point>,
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
            path
        )
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