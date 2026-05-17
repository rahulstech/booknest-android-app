package rahulstech.android.booknest.ui.screen.hotelbooking

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import rahulstech.android.booknest.R
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.model.PlaceName
import rahulstech.android.booknest.ui.component.DatePickerComposableDialog
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.theme.BookNestTheme
import rahulstech.android.booknest.util.sampleLocations
import rahulstech.android.booknest.util.samplePlaces
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

private val DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy")

@Composable
fun FindRoomRoute(
    onLogout: ()-> Unit,
    onViewAllPlaces: ()-> Unit,
    onViewPlace: (String)-> Unit,
    onSearch: (RoomSearchParameter) -> Unit,
    viewModel: BookHotelViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    FindRoomScreen(
        uiState = uiState,
        onChangeLocation = viewModel::updateLocation,
        onChangeCheckInDate = viewModel::updateCheckInDate,
        onChangeCheckOutDate = viewModel::updateCheckOutDate,
        onChangeRoom = viewModel::updateRooms,
        onLogout = onLogout,
        onSearch = onSearch,
        onViewAllPlaces = onViewAllPlaces,
        onViewPlace = onViewPlace
    )
}

/**
 * FindRoomScreen — search form + best-places horizontal list.
 *
 * The Bottom Navigation bar is owned by the parent Scaffold; it is NOT included here.
 *
 * @param onLogout  Invoked when the user taps Logout.
 * @param onSearch  Invoked with the form values when SEARCH is tapped.
 * @param onViewAllPlaces Invoked when "VIEW ALL" is tapped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(
    uiState: FindRoomUIState,
    onChangeLocation: (PlaceName)-> Unit,
    onChangeCheckInDate: (LocalDate)-> Unit,
    onChangeCheckOutDate: (LocalDate)-> Unit,
    onChangeRoom: (Int)-> Unit,
    onLogout: () -> Unit,
    onSearch: (RoomSearchParameter) -> Unit,
    onViewAllPlaces: () -> Unit,
    onViewPlace: (String)-> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.find_room_title),
                showLogoutAction = true,
                onLogout = onLogout
            )
        }
    ) { paddingValues ->
        // -------------------------------------------------------------------
        // Scrollable body
        // -------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                    LocationChooser(
                        allLocations = uiState.allLocations,
                        location = uiState.location,
                        onChange = onChangeLocation
                    )

                    // Check-in Date
                    DateChooser(
                        label = stringResource(R.string.find_room_label_check_in),
                        date = uiState.checkIn,
                        onChange = onChangeCheckInDate
                    )

                    // Check-out Date
                    DateChooser(
                        label = stringResource(R.string.find_room_label_check_out),
                        date = uiState.checkOut,
                        onChange = onChangeCheckOutDate
                    )

                    // Number of Rooms
                    RoomChooser(
                        rooms = uiState.rooms,
                        onChange = onChangeRoom
                    )

                    // SEARCH button — top corners flush with card body, bottom corners match card
                    Button(
                        onClick  = {
                            val params = RoomSearchParameter(
                                placeId = uiState.location?.id ?: "",
                                checkIn = uiState.checkIn,
                                checkOut = uiState.checkOut,
                                rooms = uiState.rooms
                            )
                            onSearch(params)
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

            SectionBestPlaces(
                places = uiState.bestPlaces,
                onViewAll = onViewAllPlaces,
                onViewPlace = onViewPlace
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationChooser(
    allLocations: List<PlaceName>,
    location: PlaceName?,
    onChange: (PlaceName)->Unit
) {

    var expanded by remember { mutableStateOf(false) }

    // Location — wrapped in a Box so the DropdownMenu anchors correctly
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        PickerTextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            label    = stringResource(R.string.find_room_label_location),
            value    = location?.name ?: "",
            leadingIcon = Icons.Default.LocationOn,
            trailingIcon = {
                Icon(
                    imageVector        = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            onClick  = { expanded = !expanded },
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
            modifier         = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            allLocations.forEach { place ->
                DropdownMenuItem(
                    text    = {
                        Text(
                            text       = place.name,
                            style      = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    onClick = {
                        onChange(place)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun DateChooser(
    label: String,
    date: LocalDate,
    onChange: (LocalDate)-> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    PickerTextField(
        label        = label,
        value        = date.format(DATE_FORMAT),
        leadingIcon  = Icons.Default.DateRange,
        onClick      = {
            showDialog = true
        },
    )

    if (showDialog) {
        DatePickerComposableDialog(
            onDismissRequest = { showDialog = false },
            title = stringResource(R.string.find_room_dialog_check_in_title),
            initialDate = date,
            onDateChanged = onChange
        )
    }
}

@Composable
private fun RoomChooser(
    rooms: Int,
    onChange: (Int)-> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    PickerTextField(
        label        = stringResource(R.string.find_room_label_rooms),
        value        = rooms.toString(),
        leadingIcon  = Icons.Default.Home,
        onClick      = { showDialog = true },
    )

    if (showDialog) {
        NumberOfRoomsDialog(
            initialCount = rooms,
            onConfirm    = { count ->
                showDialog = false
                onChange(count)
            },
        )
    }
}


@Composable
private fun SectionBestPlaces(
    places: List<Place>,
    onViewAll: () -> Unit,
    onViewPlace: (String)-> Unit
) {
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
            PlaceCard(
                place = place,
                onClick = { onViewPlace(it.id) }
            )
        }
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
                    indication = null,
                    onClick = onClick,
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
private fun PlaceCard(
    place: Place,
    onClick: (Place) -> Unit
) {
    Card(
        onClick = { onClick(place) } ,
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

@Preview(name = "FindRoom", showBackground = true, showSystemUi = true)
@Composable
private fun FindRoomEmptyPreview() {
    BookNestTheme {
        FindRoomScreen(
            uiState = FindRoomUIState(
                allLocations = sampleLocations,
                bestPlaces = samplePlaces
            ),
            onChangeLocation = {},
            onChangeCheckInDate = {},
            onChangeCheckOutDate = {},
            onChangeRoom = {},
            onLogout = {},
            onSearch = {},
            onViewAllPlaces = {},
            onViewPlace = {}
        )
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
