package rahulstech.android.booknest.ui.screen.where2go

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.samplePlaces

private const val TAG = "Where2Go"

@Composable
fun Where2GoRoute(
    onExit: ()-> Unit,
    onLogout: () -> Unit,
    onViewPlace: (String)-> Unit,
    viewModel: Where2GoViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadPlaces()
    }

    val uiState by viewModel.uiState

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.where2go_title),
                showNavUp = true,
                onNavUp = onExit,
                showLogoutAction = true,
                onLogout = onLogout
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(paddingValues)
                        .size(size = 64.dp)
                )
            }
            else if (uiState.error != null) {
                // TODO: handle load error
            }
            else {
                Where2GoScreen(
                    places = uiState.allPlaces,
                    onViewPlace = onViewPlace,
                )
            }
        }
    }
}

@Composable
fun Where2GoScreen(
    places: List<Place>,
    onViewPlace: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = places, key = { it.id }) { place ->
            CityCard(
                place = place,
                onClick =  { onViewPlace(it.id) }
            )
        }
    }
}

@Composable
private fun CityCard(
    place: Place,
    onClick: (Place)-> Unit,
    modifier: Modifier = Modifier
) {
//    Log.d(TAG,"place = $place")

    Card(
        onClick = { onClick(place) },
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
        Where2GoScreen(
            places = samplePlaces,
            onViewPlace = {}
        )
    }
}
