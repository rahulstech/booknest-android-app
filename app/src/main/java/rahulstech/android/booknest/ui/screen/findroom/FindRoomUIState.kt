package rahulstech.android.booknest.ui.screen.findroom

import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.model.PlaceName
import rahulstech.android.booknest.util.samplePlaces
import java.time.LocalDate

data class FindRoomUIState(
    val allLocations: List<PlaceName>,
    val bestPlaces: List<Place> = samplePlaces,
    val location: PlaceName? = null,
    val rooms: Int = 1,
    val checkIn: LocalDate = LocalDate.now(),
    val checkOut: LocalDate = LocalDate.now(),
)
