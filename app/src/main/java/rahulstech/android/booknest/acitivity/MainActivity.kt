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
import androidx.compose.runtime.getValue
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
import rahulstech.android.booknest.ui.theme.BookNestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookNestTheme {
                val currentUser by Authenticator.instance.currentUser
                MainScreen(currentUser != null)
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MainScreen(isLoggedIn: Boolean = true) {
    val navController = rememberNavController()

    fun onLogout() {
        Authenticator.instance.logout()
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) RootGraphRoute.Main else RootGraphRoute.Auth
    ) {

        composable<RootGraphRoute.Auth> {
            AuthRoute()
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