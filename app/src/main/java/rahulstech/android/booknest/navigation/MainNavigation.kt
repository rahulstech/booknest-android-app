package rahulstech.android.booknest.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rahulstech.android.booknest.acitivity.BottomNavDestinations
import rahulstech.android.booknest.ui.screen.hotelbooking.BookHotelViewModel
import rahulstech.android.booknest.ui.screen.hotelbooking.CheckoutRoute
import rahulstech.android.booknest.ui.screen.hotelbooking.FindRoomRoute
import rahulstech.android.booknest.ui.screen.hotelbooking.SelectHotelRoute
import rahulstech.android.booknest.ui.screen.hotelbooking.SelectRoomRoute
import rahulstech.android.booknest.ui.screen.place.PlaceRoute
import rahulstech.android.booknest.ui.screen.where2go.Where2GoRoute

sealed interface MainGraphRoute {

    @Serializable
    object BookHotel: MainGraphRoute

    @Serializable
    object FindRoom: MainGraphRoute

    @Serializable
    data class Place(val placeId: String): MainGraphRoute

    @Serializable
    data class SelectHotel(val placeId: String): MainGraphRoute

    @Serializable
    data class SelectRoom(val hotelId: String): MainGraphRoute

    @Serializable
    object Checkout: MainGraphRoute

    @Serializable
    object Where2Go: MainGraphRoute

    @Serializable
    object FAQ: MainGraphRoute
}

@Composable
fun MainRoute(
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()

    fun onExit() { navController.popBackStack() }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            BottomNavDestinations.entries.forEach { dest ->
                item(
                    icon = {
                        Icon(
                            dest.icon,
                            contentDescription = stringResource(dest.labelRes)
                        )
                    },
                    label = { Text(stringResource(dest.labelRes)) },
                    selected = navController.currentDestination
                        ?.hierarchy
                        ?.any { it.hasRoute(dest.appRoute::class) } == true,
                    onClick = {
                        navController.navigate(dest.appRoute)
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = MainGraphRoute.BookHotel,
        ) {

            // home
            bookHotelGraph(
                navController = navController,
                onExit = { onExit() },
                onLogout = onLogout
            )

            // where2go
            composable<MainGraphRoute.Where2Go> {
                Where2GoRoute(
                    onExit = { onExit() },
                    onLogout = onLogout,
                    onViewPlace = { placeId ->
                        navController.navigate(MainGraphRoute.Place(placeId))
                    }
                )
            }

            // about place
            composable<MainGraphRoute.Place> { backStack ->
                val place = backStack.toRoute<MainGraphRoute.Place>()
                PlaceRoute(
                    placeId = place.placeId,
                    onExit = { onExit() },
                    onLogout = onLogout,
                )
            }

            composable<MainGraphRoute.FAQ> {
                // TODO: add Faq route
            }
        }
    }
}

private fun NavGraphBuilder.bookHotelGraph(
    navController: NavController,
    onExit: ()-> Unit,
    onLogout: ()-> Unit,
) {
    navigation<MainGraphRoute.BookHotel>(
        startDestination = MainGraphRoute.FindRoom
    ) {
        // find room
        composable<MainGraphRoute.FindRoom> { backStack ->
            val graphEntry = remember(backStack) { navController.getBackStackEntry(MainGraphRoute.BookHotel::class) }
            val viewModel = viewModel<BookHotelViewModel>(graphEntry)
            FindRoomRoute(
                viewModel = viewModel,
                onLogout = onLogout,
                onViewAllPlaces = { navController.navigate(MainGraphRoute.Where2Go) },
                onViewPlace = { placeId ->
                    navController.navigate(MainGraphRoute.Place(placeId))
                },
                onSearch = { params ->
                    viewModel.roomSearchParameter = params
                    navController.navigate(MainGraphRoute.SelectHotel(params.placeId))
                }
            )
        }

        // select hotel
        composable<MainGraphRoute.SelectHotel> { backStack ->
            val graphEntry = remember(backStack) { navController.getBackStackEntry(MainGraphRoute.BookHotel::class) }
            val route = backStack.toRoute<MainGraphRoute.SelectHotel>()
            SelectHotelRoute(
                viewModel = viewModel(graphEntry),
                placeId = route.placeId,
                onExit = onExit,
                onLogout = onLogout,
                onViewHotel = { hotelId ->
                    navController.navigate(MainGraphRoute.SelectRoom(hotelId))
                }
            )
        }

        // select room
        composable<MainGraphRoute.SelectRoom> { backStack ->
            val graphEntry = remember(backStack) { navController.getBackStackEntry(MainGraphRoute.BookHotel::class) }
            val route = backStack.toRoute<MainGraphRoute.SelectRoom>()
            val viewModel = viewModel<BookHotelViewModel>(graphEntry)
            SelectRoomRoute(
                viewModel = viewModel,
                hotelId = route.hotelId,
                params = viewModel.roomSearchParameter,
                onExit = onExit,
                onLogout = onLogout,
                onCheckout = {
                    navController.navigate(MainGraphRoute.Checkout)
                }
            )
        }

        // checkout
        composable<MainGraphRoute.Checkout> { backStack ->
            val graphEntry = remember(backStack) { navController.getBackStackEntry(MainGraphRoute.BookHotel::class) }
            val viewModel = viewModel<BookHotelViewModel>(graphEntry)
            CheckoutRoute(
                viewModel = viewModel,
                params = viewModel.roomSearchParameter,
                onExit = onExit,
                onLogout = onLogout,
            )
        }
    }
}
