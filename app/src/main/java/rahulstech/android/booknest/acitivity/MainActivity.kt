package rahulstech.android.booknest.acitivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rahulstech.android.booknest.R
import rahulstech.android.booknest.auth.Authenticator
import rahulstech.android.booknest.navigation.AuthRoute
import rahulstech.android.booknest.navigation.MainGraphRoute
import rahulstech.android.booknest.navigation.MainRoute
import rahulstech.android.booknest.navigation.RootGraphRoute
import rahulstech.android.booknest.ui.screen.splash.SplashScreen
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

    fun onLogout() {
        Authenticator.instance.logout()
        navController.navigate(RootGraphRoute.Auth) {
            popUpTo(RootGraphRoute.Main) {
                inclusive = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = RootGraphRoute.Splash
    ) {

        composable<RootGraphRoute.Splash> {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(RootGraphRoute.Main) {
                        popUpTo(RootGraphRoute.Splash) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(RootGraphRoute.Auth) {
                        popUpTo(RootGraphRoute.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<RootGraphRoute.Auth> {
            AuthRoute(
                onNavigateToMain = {
                    navController.navigate(RootGraphRoute.Main) {
                        popUpTo(RootGraphRoute.Auth){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<RootGraphRoute.Main> {
            MainRoute(
                onLogout = { onLogout() }
            )
        }
    }
}

enum class BottomNavDestinations(
    val labelRes: Int,
    val icon: ImageVector,
    val appRoute: MainGraphRoute
) {
    HOME(R.string.nav_home, Icons.Default.Home, MainGraphRoute.FindRoom),
    WHERE2GO(R.string.nav_where2go, Icons.Outlined.LocationOn, MainGraphRoute.Where2Go),
    FAQ(R.string.nav_faqs, Icons.Outlined.Info, MainGraphRoute.FAQ)
}