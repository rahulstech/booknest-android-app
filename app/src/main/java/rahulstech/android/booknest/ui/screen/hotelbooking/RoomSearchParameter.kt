package rahulstech.android.booknest.ui.screen.hotelbooking

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class RoomSearchParameter(
    val placeId: String = "",
    val checkIn: LocalDate = LocalDate.now(),
    val checkOut: LocalDate = LocalDate.now(),
    val rooms: Int = 1
) {
    fun calculateDays(): Int = (checkOut.toEpochDay() - checkIn.toEpochDay()).toInt() + 1
}