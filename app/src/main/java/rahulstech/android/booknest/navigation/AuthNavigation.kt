package rahulstech.android.booknest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import rahulstech.android.booknest.ui.screen.signup.AuthViewModel
import rahulstech.android.booknest.ui.screen.signup.SignupRoute
import rahulstech.android.booknest.ui.screen.signup.VerifyOtpRoute

sealed interface AuthGraphRoute {

    @Serializable
    object SingUp: AuthGraphRoute

    @Serializable
    object VerifyOtp: AuthGraphRoute

}

@Composable
fun AuthRoute() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthGraphRoute.SingUp
    ) {

        composable<AuthGraphRoute.SingUp> { backStack ->
            val graphEntry = remember(backStack) { navController.getBackStackEntry<AuthGraphRoute.SingUp>() }
            val viewModel = viewModel<AuthViewModel>(graphEntry)
            SignupRoute(
                viewModel = viewModel,
                onOtpSent = {
                    navController.navigate(AuthGraphRoute.VerifyOtp)
                }
            )
        }

        composable<AuthGraphRoute.VerifyOtp> { backStack ->
            val graphEntry = remember(backStack) { navController.getBackStackEntry<AuthGraphRoute.SingUp>() }
            val viewModel = viewModel<AuthViewModel>(graphEntry)
            VerifyOtpRoute(
                viewModel = viewModel,
                onExit = { navController.popBackStack() },
            )
        }
    }
}