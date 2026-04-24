package rahulstech.android.booknest.ui.screen.findroom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.DatePickerComposableDialog
import rahulstech.android.booknest.ui.component.LocalDateSaver
import rahulstech.android.booknest.ui.theme.BookNestTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

private val DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy")


/**
 * FindRoomScreen — search form + best-places horizontal list.
 *
 * The Bottom Navigation bar is owned by the parent Scaffold; it is NOT included here.
 *
 * @param onLogout  Invoked when the user taps Logout.
 * @param onSearch  Invoked with the form values when SEARCH is tapped.
 * @param onViewAll Invoked when "VIEW ALL" is tapped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(
    locations: List<String>,
    places: List<Place> = emptyList(),
    onLogout: () -> Unit = {},
    onSearch: (location: String, checkIn: LocalDate, checkOut: LocalDate, rooms: Int) -> Unit = { _, _, _, _ -> },
    onViewAll: () -> Unit = {},
) {
    var selectedLocation         by rememberSaveable { mutableStateOf("") }
    var numberOfRooms            by rememberSaveable { mutableIntStateOf(0) }
    var locationDropdownExpanded by remember { mutableStateOf(false) }
    var showRoomsDialog          by remember { mutableStateOf(false) }
    var showCheckInDatePickerDialog by remember { mutableStateOf(false) }
    var showCheckOutDatePickerDialog by remember { mutableStateOf(false) }
    var checkInDate by rememberSaveable(stateSaver = LocalDateSaver()) { mutableStateOf<LocalDate?>(null) }
    var checkOutDate by rememberSaveable(stateSaver = LocalDateSaver()) { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // -------------------------------------------------------------------
        // Top App Bar
        // -------------------------------------------------------------------
        TopAppBar(
            title = {
                Text(
                    text       = stringResource(R.string.find_room_title),
                    style      = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onSurface,
                )
            },
            actions = {
                FilledTonalButton(
                    onClick = onLogout,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text  = stringResource(R.string.find_room_logout),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        )

        // -------------------------------------------------------------------
        // Scrollable body
        // -------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
        ) {
            // ---------------------------------------------------------------
            // Search form card
            // ---------------------------------------------------------------
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors    = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    // Location — wrapped in a Box so the DropdownMenu anchors correctly
                    Box(modifier = Modifier.fillMaxWidth()) {
                        PickerTextField(
                            label    = stringResource(R.string.find_room_label_location),
                            value    = selectedLocation,
                            leadingIcon = Icons.Default.LocationOn,
                            trailingIcon = {
                                Icon(
                                    imageVector        = if (locationDropdownExpanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                            onClick  = { locationDropdownExpanded = true },
                        )
                        DropdownMenu(
                            expanded         = locationDropdownExpanded,
                            onDismissRequest = { locationDropdownExpanded = false },
                            modifier         = Modifier.widthIn(max = 300.dp).background(MaterialTheme.colorScheme.surfaceVariant),
                        ) {
                            locations.forEach { city ->
                                DropdownMenuItem(
                                    text    = {
                                        Text(
                                            text       = city,
                                            style      = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    },
                                    onClick = {
                                        selectedLocation = city
                                        locationDropdownExpanded = false
                                    },
                                )
                            }
                        }
                    }

                    // Check-in Date
                    PickerTextField(
                        label        = stringResource(R.string.find_room_label_check_in),
                        value        = checkInDate?.format(DATE_FORMAT) ?: "",
                        leadingIcon  = Icons.Default.DateRange,
                        onClick      = {
                            showCheckInDatePickerDialog = true
                        },
                    )

                    // Check-out Date
                    PickerTextField(
                        label        = stringResource(R.string.find_room_label_check_out),
                        value        = checkOutDate?.format(DATE_FORMAT) ?: "",
                        leadingIcon  = Icons.Default.DateRange,
                        onClick      = {
                            showCheckOutDatePickerDialog = true
                        },
                    )

                    // Number of Rooms
                    PickerTextField(
                        label        = stringResource(R.string.find_room_label_rooms),
                        value        = if (numberOfRooms > 0) numberOfRooms.toString() else "",
                        leadingIcon  = Icons.Default.Home,
                        onClick      = { showRoomsDialog = true },
                    )

                    // SEARCH button — top corners flush with card body, bottom corners match card
                    Button(
                        onClick  = {
                            if (null != checkInDate && null != checkOutDate) {
                                onSearch(selectedLocation, checkInDate!!, checkOutDate!!, numberOfRooms)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape    = RoundedCornerShape(
                            topStart    = 0.dp, topEnd    = 0.dp,
                            bottomStart = 8.dp, bottomEnd = 8.dp,
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor   = MaterialTheme.colorScheme.onPrimary,
                        ),
                    ) {
                        Text(
                            text          = stringResource(R.string.find_room_btn_search),
                            style         = MaterialTheme.typography.labelLarge,
                            letterSpacing = 2.sp,
                            fontWeight    = FontWeight.Bold,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ---------------------------------------------------------------
            // Best Places header
            // ---------------------------------------------------------------
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text(
                    text          = stringResource(R.string.find_room_best_places),
                    style         = MaterialTheme.typography.labelLarge,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color         = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = onViewAll) {
                    Text(
                        text       = stringResource(R.string.find_room_view_all),
                        style      = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---------------------------------------------------------------
            // Horizontal place cards
            // ---------------------------------------------------------------
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(
                    items = places,
                    key = { it.id }
                ) { place ->
                    PlaceCard(place = place)
                }
            }
        }
    }

    if (showRoomsDialog) {
        NumberOfRoomsDialog(
            initialCount = numberOfRooms.coerceAtLeast(1),
            onConfirm    = { count ->
                showRoomsDialog = false
                numberOfRooms = count
            },
        )
    }

    if (showCheckInDatePickerDialog) {
        DatePickerComposableDialog(
            onDismissRequest = { showCheckInDatePickerDialog = false },
            title = stringResource(R.string.find_room_dialog_check_in_title),
            initialDate = LocalDate.now(),
            onDateChanged = { newCheckInDate ->
                checkInDate = newCheckInDate
            }
        )
    }

    if (showCheckOutDatePickerDialog) {
        DatePickerComposableDialog(
            onDismissRequest = { showCheckOutDatePickerDialog = false },
            title = stringResource(R.string.find_room_dialog_check_out_title),
            initialDate = LocalDate.now(),
            onDateChanged = { newCheckOutDate ->
                checkOutDate = newCheckOutDate
            }
        )
    }

}

// ---------------------------------------------------------------------------
// PickerTextField
// ---------------------------------------------------------------------------

/**
 * A read-only [TextField] that looks like a standard Material text field with a
 * **bottom indicator line only** (no outline), a floating label, and a leading icon.
 *
 * Because the value always comes from an external picker (dropdown / dialog / date
 * picker) the field is [enabled] = false so no keyboard or cursor ever appears.
 * A transparent clickable overlay on top routes taps to [onClick].
 */
@Composable
private fun PickerTextField(
    label: String,
    value: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    val hasValue = value.isNotBlank()

    Box(modifier = modifier.fillMaxWidth()) {
        TextField(
            value         = value,
            onValueChange = {},
            modifier      = Modifier.fillMaxWidth(),
            enabled       = false,      // no keyboard, no cursor, no focus ring
            singleLine    = true,
            label         = { Text(text = label, style = MaterialTheme.typography.bodyLarge) },
            leadingIcon   = {
                Icon(
                    imageVector        = leadingIcon,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.primary,
                )
            },
            trailingIcon  = trailingIcon,
            colors        = TextFieldDefaults.colors(
                disabledContainerColor   = Color.Transparent,
                focusedContainerColor    = Color.Transparent,
                unfocusedContainerColor  = Color.Transparent,
                disabledIndicatorColor   = MaterialTheme.colorScheme.outlineVariant,
                focusedIndicatorColor    = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor  = MaterialTheme.colorScheme.outlineVariant,
                disabledTextColor        = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor       = if (hasValue) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLabelColor        = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor      = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor  = MaterialTheme.colorScheme.primary,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    onClick           = onClick,
                )
        )
    }
}

// ---------------------------------------------------------------------------
// NumberOfRoomsDialog
// ---------------------------------------------------------------------------

@Composable
private fun NumberOfRoomsDialog(
    initialCount: Int,
    onConfirm: (Int) -> Unit,
) {
    var count by remember { mutableIntStateOf(initialCount) }

    AlertDialog(
        onDismissRequest = { onConfirm(count) },
        shape            = MaterialTheme.shapes.extraLarge,
        containerColor   = MaterialTheme.colorScheme.surfaceVariant,
        title            = {
            Text(
                text      = stringResource(R.string.find_room_label_rooms),
                style     = MaterialTheme.typography.titleLarge,
                color     = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth(),
            )
        },
        text = {
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick  = { if (count > 1) count-- },
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurface,
                        modifier           = Modifier.size(32.dp),
                    )
                }

                Text(
                    text  = count.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                IconButton(
                    onClick  = { count++ },
                    modifier = Modifier.size(48.dp),
                ) {
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurface,
                        modifier           = Modifier.size(32.dp),
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
    )
}

// ---------------------------------------------------------------------------
// PlaceCard
// ---------------------------------------------------------------------------

@Composable
private fun PlaceCard(place: Place) {
    Card(
        modifier  = Modifier
            .width(160.dp)
            .height(160.dp),
        shape     = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model              = place.imageUrl,
                contentDescription = place.name,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.scrim.copy(alpha = 0f),
                                MaterialTheme.colorScheme.scrim.copy(alpha = 0.65f),
                            ),
                        ),
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                Text(
                    text       = place.name,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------

private val sampleLocations = listOf(
    "Agra", "Bengaluru", "Chennai", "Delhi", "Mumbai", "Kolkata", "Jaipur"
)

private val samplePlaces = listOf(
    Place(
        id = "place-1",
        name     = "Agra",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1d/Taj_Mahal_%28Edited%29.jpeg/1200px-Taj_Mahal_%28Edited%29.jpeg"
    ),
    Place(
        id = "place-2",
        name     = "Bengaluru",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/09/KR_Puram_Bridge.jpg/1200px-KR_Puram_Bridge.jpg"
    ),
    Place(
        id = "place-3",
        name     = "Chennai",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Chennai_Montage.jpg/800px-Chennai_Montage.jpg"
    ),
    Place(
        id = "place-4",
        name     = "Delhi",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/India_Gate_in_New_Delhi_03-2016.jpg/1200px-India_Gate_in_New_Delhi_03-2016.jpg"
    ),
    Place(
        id = "place-5",
        name     = "Mumbai",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/Mumbai_03-2016_30_Gateway_of_India.jpg/1200px-Mumbai_03-2016_30_Gateway_of_India.jpg"
    ),
)

@Preview(name = "FindRoom – Empty", showBackground = true, showSystemUi = true)
@Composable
private fun FindRoomEmptyPreview() {
    BookNestTheme {
        FindRoomScreen(
            locations = sampleLocations,
            places = samplePlaces
        )
    }
}

@Preview(name = "FindRoom – Filled", showBackground = true, showSystemUi = true)
@Composable
private fun FindRoomFilledPreview() {
    BookNestTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        ) {
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = MaterialTheme.shapes.small,
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column {
                    PickerTextField(
                        label       = stringResource(R.string.find_room_label_location),
                        value       = "Agra",
                        leadingIcon = Icons.Default.LocationOn,
                        trailingIcon = {
                            Icon(
                                imageVector        = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                    )
                    PickerTextField(
                        label       = stringResource(R.string.find_room_label_check_in),
                        value       = "19/6/2024",
                        leadingIcon = Icons.Default.DateRange,
                    )
                    PickerTextField(
                        label       = stringResource(R.string.find_room_label_check_out),
                        value       = "21/6/2024",
                        leadingIcon = Icons.Default.DateRange,
                    )
                    PickerTextField(
                        label       = stringResource(R.string.find_room_label_rooms),
                        value       = "2",
                        leadingIcon = Icons.Default.Home,
                    )
                    Button(
                        onClick  = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape    = RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp,
                            bottomStart = 8.dp, bottomEnd = 8.dp,
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        Text(
                            text          = stringResource(R.string.find_room_btn_search),
                            style         = MaterialTheme.typography.labelLarge,
                            letterSpacing = 2.sp,
                            fontWeight    = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "NumberOfRooms Dialog", showBackground = true)
@Composable
private fun NumberOfRoomsDialogPreview() {
    BookNestTheme {
        NumberOfRoomsDialog(
            initialCount = 2,
            onConfirm    = {},
        )
    }
}
