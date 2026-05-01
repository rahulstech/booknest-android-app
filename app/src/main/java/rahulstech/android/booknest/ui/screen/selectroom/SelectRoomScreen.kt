package rahulstech.android.booknest.ui.screen.selectroom

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.formatIndian

// ─────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────

/**
 * @param hotel          Hotel details to display.
 * @param rooms          List of rooms available in the hotel.
 * @param numberOfDays   Duration of the stay (passed from the search/previous screen).
 * @param onCheckout     Called when the user taps Checkout; receives the list of selected rooms.
 */
@Composable
fun SelectRoomScreen(
    hotel: HotelDetails,
    rooms: List<RoomDetails>,
    numberOfDays: Int,
    onCheckout: (selectedRooms: List<RoomDetails>) -> Unit
) {
    BookNestTheme {
        // Track which room IDs are selected
        val selectedRoomIds = remember { mutableStateListOf<String>() }

        val selectedRooms = rooms.filter { it.id in selectedRoomIds }
        val totalCost = selectedRooms.sumOf { it.pricePerDay } * numberOfDays
        val showBottomBar = selectedRoomIds.isNotEmpty()

        Box(modifier = Modifier.fillMaxSize()) {

            // ── Scrollable content ──────────────────────────────
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = if (showBottomBar) 100.dp else 16.dp
                )
            ) {

                // Hero image
                item {
                    HeroImage(url = hotel.heroPhotoUrl)
                }

                // Hotel name & rating
                item {
                    HotelHeader(
                        name = stringResource(R.string.select_room_hotel_header_format, hotel.name, hotel.location),
                        stars = hotel.stars
                    )
                }

                // About the hotel
                item {
                    SectionTitle(text = stringResource(R.string.select_room_about_hotel))
                    Text(
                        text = hotel.aboutTheHotel,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Justify
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }

                // Amenities
                item {
                    SectionTitle(text = stringResource(R.string.select_room_amenities_available))
                    AmenitiesRow(amenities = hotel.amenities)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }

                // Property rules & information
                item {
                    SectionTitle(text = stringResource(R.string.select_room_property_rules))
                    hotel.rulesAndInformation.forEach { rule ->
                        Text(
                            text = "○  $rule",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Select rooms heading
                item {
                    Text(
                        text = stringResource(R.string.select_room_title_select_rooms),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Room cards
                items(rooms, key = { it.id }) { room ->
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
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
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
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun AmenitiesRow(amenities: List<Amenity>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
            contentDescription = amenity.label,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amenity.label,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
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

        Spacer(modifier = Modifier.height(10.dp))

        // Name, price and select button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = room.roomType,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.select_room_price_format, room.pricePerDay.formatIndian()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            SelectButton(isSelected = isSelected, onClick = onToggle)
        }
    }
}

@Composable
private fun SelectButton(isSelected: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                             else Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = if (isSelected) stringResource(R.string.select_room_btn_selected) else stringResource(R.string.select_room_btn_select),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
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
    Box(
        modifier = Modifier
            .padding(16.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF29B6F6),  // light blue
                        Color(0xFF00ACC1)   // cyan
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val roomsText = pluralStringResource(R.plurals.select_room_label_room, roomCount, roomCount)
            val daysText = pluralStringResource(R.plurals.select_room_label_day, days, days)
            Text(
                text = stringResource(R.string.select_room_checkout_format, roomsText, daysText, totalCost.formatIndian()),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Button(
                onClick = onCheckout,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_room_btn_checkout),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────

// sample data

fun sampleHotel() = HotelDetails(
    heroPhotoUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800",
    name = "Fairfield Hotel",
    location = "Agra",
    stars = 4.3f,
    aboutTheHotel = "Our hotel is located in Ashok Cosmos Mall in Sanjay Place, one of the " +
            "largest shopping malls in the city. Iconic sites such as the Taj Mahal are minutes " +
            "away while fun outings to Agra Golf Course are just around the corner.",
    amenities = listOf(
        Amenity(R.drawable.ic_gym, "Gym"),
        Amenity(R.drawable.ic_free_parking, "Free Parking"),
        Amenity(R.drawable.ic_restaurant, "Restaurant")
    ),
    rulesAndInformation = listOf(
        "Check-in: 12.00 Pm, Check-out: 11.00 Am",
        "Pets are not allowed.",
        "Outside food is not allowed",
        "Passport, Aadhar, and Govt. ID are accepted as ID proofs."
    )
)

fun sampleRooms() = listOf(
    RoomDetails(
        id = "1",
        roomType = "Deluxe Room AC",
        pricePerDay = 4999,
        photoUrl = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=800"
    ),
    RoomDetails(
        id = "2",
        roomType = "Double Room",
        pricePerDay = 7999,
        photoUrl = "https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=800"
    ),
    RoomDetails(
        id = "3",
        roomType = "Executive",
        pricePerDay = 9999,
        photoUrl = "https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=800"
    )
)

@Preview(showBackground=true)
@Composable
fun SelectRoomScreenPreview() {
    BookNestTheme {
        SelectRoomScreen(
            hotel = sampleHotel(),
            rooms = sampleRooms(),
            numberOfDays = 2,
            onCheckout = {}
        )
    }
}
