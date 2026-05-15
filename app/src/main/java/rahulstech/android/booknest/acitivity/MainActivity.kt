package rahulstech.android.booknest.acitivity

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.screen.findroom.FindRoomRoute
import rahulstech.android.booknest.ui.screen.place.PlaceRoute
import rahulstech.android.booknest.ui.screen.where2go.Where2GoRoute
import rahulstech.android.booknest.ui.theme.BookNestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookNestTheme {
                MainScreen()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var currentDestination by rememberSaveable { mutableStateOf(BottomNavDestinations.HOME) }

    val onExit: ()-> Unit = { navController.popBackStack() }

    val onLogout: ()-> Unit = { }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            BottomNavDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.labelRes)
                        )
                    },
                    label = { Text(stringResource(it.labelRes)) },
                    selected = it == currentDestination,
                    onClick = {
                        currentDestination = it
                        navController.navigate(it.appRoute.route)
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = AppRoute.FindRoom.route
        ) {

            // find room
            composable(route = AppRoute.FindRoom.route) {
                FindRoomRoute(
                    onLogout = onLogout,
                    onViewAllPlaces = { navController.navigate(AppRoute.Where2Go.route) },
                    onViewPlace = { placeId ->
                        navController.navigate(AppRoute.Place.build(placeId))
                    }
                )
            }

            // where2go
            composable(route = AppRoute.Where2Go.route) {
                Where2GoRoute(
                    onExit = onExit,
                    onLogout = onLogout,
                    onViewPlace = { navController.navigate(AppRoute.Place.build(it)) }
                )
            }

            // faq
            composable(route = AppRoute.FAQ.route) {
                // TODO: add faq route
            }

            // about place
            composable(
                route = AppRoute.Place.route,
                arguments = listOf(
                    navArgument("placeId") {
                        type = NavType.StringType
                        nullable = false
                    }
                )
            ) { backStack ->
                val placeId = backStack.arguments?.getString("placeId")!!
                PlaceRoute(
                    placeId = placeId,
                    onExit = onExit,
                    onLogout = onLogout
                )
            }
        }
    }
}

enum class BottomNavDestinations(
    val labelRes: Int,
    val icon: ImageVector,
    val appRoute: AppRoute
) {
    HOME(R.string.nav_home, Icons.Default.Home, AppRoute.FindRoom),
    WHERE2GO(R.string.nav_where2go, Icons.Outlined.LocationOn, AppRoute.Where2Go),
    FAQ(R.string.nav_faqs, Icons.Outlined.Info, AppRoute.FAQ)
}

sealed class AppRoute(val route: String) {

    object FindRoom: AppRoute("find_room")
    object Place: AppRoute("places/{placeId}") {

        fun build(placeId: String): String = "places/$placeId"
    }

    object Where2Go: AppRoute("where2go")

    object FAQ: AppRoute("faq")

}