package rahulstech.android.booknest.ui.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class BookingDate(
    val checkInDate: LocalDate,
    val checkOutDate: LocalDate
) {
    val numberOfDays: Int
        get() = ChronoUnit.DAYS.between(checkInDate, checkOutDate).toInt()
}
