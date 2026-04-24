package rahulstech.android.booknest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import rahulstech.android.booknest.ui.screen.selecthotel.FindHotelItem
import rahulstech.android.booknest.ui.screen.selecthotel.SelectHotelScreen
import rahulstech.android.booknest.ui.theme.BookNestTheme

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
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        SelectHotelScreen(hotels = listOf(
            FindHotelItem(
                id = "hotel-1",
                hotelName = "Fairfield Hotel",
                cityName = "Agra",
                priceMin = 4_999,
                priceMax = 9_999,
                isSoldOut = false,
                imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
            ),
            FindHotelItem(
                id = "hotel-2",
                hotelName = "Oberoi Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800",
            ),
            FindHotelItem(
                id = "hotel-12",
                hotelName = "Taj Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800",
            ),
            FindHotelItem(
                id = "hotel-3",
                hotelName = "Fairfield Hotel",
                cityName = "Agra",
                priceMin = 4_999,
                priceMax = 9_999,
                isSoldOut = false,
                imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
            ),
            FindHotelItem(
                id = "hotel-4",
                hotelName = "Oberoi Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800",
            ),
            FindHotelItem(
                id = "hotel-5",
                hotelName = "Taj Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800",
            ),
            FindHotelItem(
                id = "hotel-6",
                hotelName = "Fairfield Hotel",
                cityName = "Agra",
                priceMin = 4_999,
                priceMax = 9_999,
                isSoldOut = false,
                imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
            ),
            FindHotelItem(
                id = "hotel-7",
                hotelName = "Oberoi Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800",
            ),
            FindHotelItem(
                id = "hotel-8",
                hotelName = "Taj Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800",
            ),
            FindHotelItem(
                id = "hotel-9",
                hotelName = "Fairfield Hotel",
                cityName = "Agra",
                priceMin = 4_999,
                priceMax = 9_999,
                isSoldOut = false,
                imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
            ),
            FindHotelItem(
                id = "hotel-10",
                hotelName = "Oberoi Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800",
            ),
            FindHotelItem(
                id = "hotel-11",
                hotelName = "Taj Hotel",
                cityName = "Agra",
                isSoldOut = true,
                imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800",
            ),
        ))
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookNestTheme {
        Greeting("Android")
    }
}