package rahulstech.android.booknest.ui.screen.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.model.BookingDate
import rahulstech.android.booknest.ui.model.HotelDetails
import rahulstech.android.booknest.ui.model.RoomDetails
import rahulstech.android.booknest.ui.model.UserDetails
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.formatIndian
import rahulstech.android.booknest.util.sampleHotel
import rahulstech.android.booknest.util.sampleSelectedRooms
import rahulstech.android.booknest.util.sampleUserDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    hotel: HotelDetails,
    selectedRooms: List<RoomDetails>,
    bookingDate: BookingDate,
    user: UserDetails,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onRemoveRoom: (RoomDetails) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.checkout_screen_title),
                showNavUp = true,
                onNavUp = onBack,
                showLogoutAction = true,
                onLogout = onLogout
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hotel Summary Card
            HotelSummaryCard(hotel)

            // Booking Details
            BookingDetailsSection(bookingDate)

            // Selected Rooms
            selectedRooms.forEach { room ->
                RoomItem(room = room, onRemove = { onRemoveRoom(room) })
            }

            // Price Breakup
            PriceBreakupSection(selectedRooms, bookingDate.numberOfDays)

            // User Details
            UserDetailsSection(user)
        }
    }
}

@Composable
private fun HotelSummaryCard(hotel: HotelDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${hotel.name}, ${hotel.location}",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        val starColor = if (index < hotel.stars.toInt()) Color(0xFFFFC107) else Color(0xFFDDDDDD)
                        Icon(
                            painter = painterResource(id = android.R.drawable.btn_star_big_on),
                            contentDescription = null,
                            tint = starColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = hotel.stars.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            AsyncImage(
                model = hotel.heroPhotoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun BookingDetailsSection(bookingDate: BookingDate) {
    Column {
        Text(
            text = stringResource(R.string.checkout_booking_details),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.checkout_check_in),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = bookingDate.checkInDate.format(DISPLAY_DATE_FORMAT),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.checkout_screen_days, bookingDate.numberOfDays),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.checkout_check_out),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = bookingDate.checkOutDate.format(DISPLAY_DATE_FORMAT),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun RoomItem(room: RoomDetails, onRemove: () -> Unit) {
    Column {
        AsyncImage(
            model = room.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = room.roomType,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.room_price_format, room.pricePerDay.formatIndian()),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            FilledTonalButton(
                onClick = onRemove,
                modifier = Modifier.height(32.dp),
                contentPadding = ButtonDefaults.TextButtonContentPadding,
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.checkout_btn_remove),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PriceBreakupSection(rooms: List<RoomDetails>, days: Int) {
    val totalRoomPrice = rooms.sumOf { it.pricePerDay } * days
    val gst = (totalRoomPrice * 0.18).toInt()
    val totalToPay = totalRoomPrice + gst

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.checkout_price_breakup),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            PriceRow(label = stringResource(R.string.checkout_room_price), amount = totalRoomPrice)
            PriceRow(label = stringResource(R.string.checkout_gst), amount = gst)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.checkout_to_pay),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Rs. ${totalToPay.formatIndian()}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = "Rs. ${amount.formatIndian()}", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun UserDetailsSection(user: UserDetails) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.checkout_user_details),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            UserDetailRow(label = stringResource(R.string.checkout_name), value = user.name)
            UserDetailRow(label = stringResource(R.string.checkout_email_id), value = user.email)
            UserDetailRow(label = stringResource(R.string.checkout_phone_number), value = user.phone)
        }
    }
}

@Composable
private fun UserDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

//----------------------------------
//      Preview
//---------------------------------

@PreviewLightDark
@Composable
private fun CheckOutScreenPreview() {
    BookNestTheme {
        CheckOutScreen(
            hotel = sampleHotel,
            selectedRooms = sampleSelectedRooms,
            bookingDate = BookingDate(
                checkInDate = LocalDate.of(2024, 6, 19),
                checkOutDate = LocalDate.of(2024, 6, 21)
            ),
            user = sampleUserDetails,
            onBack = {},
            onLogout = {},
            onRemoveRoom = {}
        )
    }
}
