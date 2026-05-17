package rahulstech.android.booknest.navigation

import kotlinx.serialization.Serializable

sealed interface RootGraphRoute {

    @Serializable
    object Auth: RootGraphRoute

    @Serializable
    object Main: RootGraphRoute
}