package rahulstech.android.booknest.ui.screen.findroom

import rahulstech.android.booknest.data.model.PlaceName
import java.time.LocalDate

data class RoomSearchParameter(
    val location: PlaceName?,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val rooms: Int
)
