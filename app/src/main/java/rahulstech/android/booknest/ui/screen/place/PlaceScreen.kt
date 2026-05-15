package rahulstech.android.booknest.ui.screen.place

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.samplePlace

private const val TAG = "PlaceScreen"

@Composable
fun PlaceRoute(
    placeId: String,
    onExit: ()-> Unit,
    onLogout: ()-> Unit,
    viewModel: PlaceViewModel = viewModel()
) {
    LaunchedEffect(placeId) {
        viewModel.findPlace(placeId)
    }

    val uiState by viewModel.uiState

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.places_title),
                showNavUp = true,
                onNavUp = onExit,
                showLogoutAction = true,
                onLogout = onLogout
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Log.d(TAG,"loading")
            CircularProgressIndicator(modifier = Modifier.padding(paddingValues).size(size = 64.dp))
        }
        else if (uiState.isSuccessful) {
            Log.d(TAG,"place[$placeId] found")
            PlaceScreen(
                place = uiState.place!!, // PlaceUIState.place is non-null only when isLoading = false and isSuccessful = true
                modifier = Modifier.padding(paddingValues)
            )
        }
        else {
            Log.d(TAG,"place[$placeId] not found")
            onExit()
        }
    }
}


@Composable
fun PlaceScreen(
    place: Place,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = place.imageUrl,
            contentDescription = place.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(bottomEnd = 56.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceScreenPreview() {

    BookNestTheme {
        PlaceScreen(place = samplePlace)
    }
}
