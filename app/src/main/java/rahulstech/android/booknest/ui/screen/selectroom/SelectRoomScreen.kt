package rahulstech.android.booknest.ui.screen.selectroom

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.data.model.Amenity
import rahulstech.android.booknest.data.model.HotelDetails
import rahulstech.android.booknest.data.model.Resource
import rahulstech.android.booknest.data.model.RoomDetails
import rahulstech.android.booknest.ui.common.RoomSearchParameter
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.formatIndian
import rahulstech.android.booknest.util.sampleHotel

private const val TAG = "SelectRoomScreen"

@Composable
fun SelectRoomRoute(
    hotelId: String,
    params: RoomSearchParameter,
    onExit: ()-> Unit,
    onLogout: ()-> Unit,
    viewModel: SelectRoomViewModel = viewModel()
) {
    Log.d(TAG, "params = $params")

    LaunchedEffect(hotelId) {
        viewModel.findHotel(hotelId)
    }

    val hotelResource by viewModel.hotelResource

    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.select_room_screen_title),
                showNavUp = true,
                onNavUp = onExit,
                showLogoutAction = true,
                onLogout = onLogout
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxWidth().padding(16.dp)
        ) {
            when(hotelResource) {
                is Resource.Loading -> {
                    // TODO: show skeleton
                    Log.d(TAG,"loading hotel[$hotelId]")
                    CircularProgressIndicator(modifier = Modifier.size(size = 64.dp))
                }

                is Resource.Success -> {
                    val hotel = (hotelResource as Resource.Success<HotelDetails?>).data
                    Log.d(TAG,"hotel[$hotelId] = $hotel")
                    if (hotel == null) {
                        onExit()
                    }
                    else {
                        SelectRoomScreen(
                            hotel = hotel,
                            numberOfDays = params.calculateDays(),
                            onCheckout = { }
                        )
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG,"hotel[$hotelId] loading error", (hotelResource as Resource.Error).cause)
                }

                else -> Unit
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────

/**
 * @param hotel          Hotel details to display.
 * @param numberOfDays   Duration of the stay (passed from the search/previous screen).
 * @param onCheckout     Called when the user taps Checkout; receives the list of selected rooms.
 */
@Composable
fun SelectRoomScreen(
    hotel: HotelDetails,
    numberOfDays: Int,
    onCheckout: (List<RoomDetails>) -> Unit
) {
    Log.d(TAG, "select rooms of hotel ${hotel.id}")
    // Track which room IDs are selected
    val selectedRoomIds = remember { mutableStateListOf<String>() }
    val selectedRooms = hotel.rooms.filter { it.id in selectedRoomIds }
    val totalCost = selectedRooms.sumOf { it.pricePerDay } * numberOfDays
    val showBottomBar = selectedRoomIds.isNotEmpty()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = if (showBottomBar) 100.dp else 0.dp
        )
    ) {

        // Hero image
        item {
            HeroImage(url = hotel.heroPhotoUrl)
        }

        // Hotel name & rating
        item {
            Spacer(modifier = Modifier.height(12.dp))

            HotelHeader(
                name = stringResource(R.string.select_room_hotel_header_format, hotel.name, hotel.location),
                stars = hotel.stars
            )
        }

        // About the hotel
        item {
            Spacer(modifier = Modifier.height(12.dp))

            SectionTitle(text = stringResource(R.string.select_room_about_hotel))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = hotel.aboutTheHotel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }

        // Amenities
        item {
            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle(text = stringResource(R.string.select_room_amenities_available))

            Spacer(modifier = Modifier.height(16.dp))

            AmenitiesRow(amenities = hotel.amenities)

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        }

        // Property rules & information
        item {
            Spacer(modifier = Modifier.height(12.dp))

            SectionTitle(text = stringResource(R.string.select_room_property_rules))

            Spacer(modifier = Modifier.height(16.dp))

            hotel.rulesAndInformation.forEach { rule ->
                Text(
                    text = "○  $rule",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        // Select rooms heading
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.select_room_title_select_rooms),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
            )
        }

        // Room cards
        items(hotel.rooms, key = { it.id }) { room ->
            val isSelected = room.id in selectedRoomIds
            RoomCard(
                room = room,
                isSelected = isSelected,
                onToggle = {
                    if (isSelected) selectedRoomIds.remove(room.id)
                    else selectedRoomIds.add(room.id)
                }
            )
        }
    }

    // ── Bottom checkout bar as a Popup ──────────────────
    if (showBottomBar) {
        Popup(
            alignment = Alignment.BottomCenter,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            CheckoutBar(
                roomCount = selectedRoomIds.size,
                days = numberOfDays,
                totalCost = totalCost,
                onCheckout = { onCheckout(selectedRooms) }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Sub-composables
// ─────────────────────────────────────────────────────────────

@Composable
private fun HeroImage(url: String) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.select_room_cd_hotel_photo),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(MaterialTheme.shapes.medium)
    )
}

@Composable
private fun HotelHeader(name: String, stars: Float) {
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        StarRating(rating = stars)
    }
}

@Composable
private fun StarRating(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        repeat(5) { index ->
            val starColor = if (index < fullStars) Color(0xFFFFC107) else Color(0xFFDDDDDD)
            Icon(
                painter = painterResource(id = android.R.drawable.btn_star_big_on),
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        ),
    )
}

@Composable
private fun AmenitiesRow(amenities: List<Amenity>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        amenities.forEach { amenity ->
            AmenityItem(amenity = amenity)
        }
    }
}

@Composable
private fun AmenityItem(amenity: Amenity) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = amenity.iconRes),
            contentDescription = stringResource(amenity.label),
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(amenity.label),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun RoomCard(
    room: RoomDetails,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Room photo
        AsyncImage(
            model = room.photoUrl,
            contentDescription = room.roomType,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Name, price and select button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.roomType,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.room_price_format, room.pricePerDay.formatIndian()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            SelectButton(isSelected = isSelected, onClick = onToggle)
        }
    }
}

@Composable
private fun SelectButton(isSelected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                             else Color.Transparent,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = if (isSelected) stringResource(R.string.select_room_btn_selected) else stringResource(R.string.select_room_btn_select),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun CheckoutBar(
    roomCount: Int,
    days: Int,
    totalCost: Int,
    onCheckout: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val roomsText = pluralStringResource(R.plurals.select_room_label_room, roomCount, roomCount)
        val daysText = pluralStringResource(R.plurals.select_room_label_day, days, days)

        Text(
            text = stringResource(R.string.select_room_checkout_format, roomsText, daysText, totalCost.formatIndian()),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Button(
            onClick = onCheckout,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
        ) {
            Text(
                text = stringResource(R.string.select_room_btn_checkout),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────

@Preview(showBackground=true)
@Composable
fun SelectRoomScreenPreview() {
    BookNestTheme {
        SelectRoomScreen(
            hotel = sampleHotel,
            numberOfDays = 2,
            onCheckout = {}
        )
    }
}
