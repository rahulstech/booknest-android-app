package rahulstech.android.booknest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import rahulstech.android.booknest.ui.screen.findroom.FindRoomScreen
import rahulstech.android.booknest.ui.screen.place.PlaceScreen
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.sampleLocations
import rahulstech.android.booknest.util.samplePlace
import rahulstech.android.booknest.util.samplePlaces

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookNestTheme {
                BookNestApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun BookNestApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.labelRes)
                        )
                    },
                    label = { Text(stringResource(it.labelRes)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when(currentDestination) {
            AppDestinations.HOME -> {
                FindRoomScreen(
                    locations = sampleLocations,
                    places = samplePlaces
                )
            }
            AppDestinations.WHERE2GO -> {
                PlaceScreen(
                    place = samplePlace
                )
            }
            AppDestinations.FAQ -> {
                Text("FAQ")
            }
        }
    }
}

enum class AppDestinations(
    val labelRes: Int,
    val icon: ImageVector,
) {
    HOME(R.string.nav_home, Icons.Default.Home),
    WHERE2GO(R.string.nav_where2go, Icons.Outlined.LocationOn),
    FAQ(R.string.nav_faqs, Icons.Outlined.Info)
}