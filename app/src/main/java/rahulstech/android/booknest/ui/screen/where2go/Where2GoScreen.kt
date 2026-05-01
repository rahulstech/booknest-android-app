package rahulstech.android.booknest.ui.screen.where2go

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.model.Place
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.samplePlaces

@Composable
fun Where2GoScreen(
    places: List<Place>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.where2go_title),
                showNavUp = true,
                onNavUp = onBack,
                showLogoutAction = true,
                onLogout = onLogout
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(places) { place ->
                CityCard(place = place)
            }
        }
    }
}

@Composable
private fun CityCard(
    place: Place,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8E0EB) // Light purple background for the bottom part
        )
    ) {
        Column {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = place.name,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Gray // Matching the slightly dimmed text color in screenshot
            )
        }
    }
}

//-------------------------------------------
//      Preview
//-------------------------------------------

@Preview(showBackground = true)
@Composable
private fun Where2GoScreenPreview() {
    BookNestTheme {
        Where2GoScreen(places = samplePlaces)
    }
}
