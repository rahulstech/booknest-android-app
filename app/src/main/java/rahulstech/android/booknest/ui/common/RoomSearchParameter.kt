package rahulstech.android.booknest.ui.common

import android.os.Bundle
import java.time.LocalDate

data class RoomSearchParameter(
    val placeId: String = "",
    val checkIn: LocalDate = LocalDate.now(),
    val checkOut: LocalDate = LocalDate.now(),
    val rooms: Int = 1
) {

    fun calculateDays(): Int = (checkOut.toEpochDay() - checkIn.toEpochDay()).toInt() + 1

    fun toBundle(): Bundle = Bundle().apply {
        putString("placeId", placeId)
        putLong("checkIn", checkIn.toEpochDay())
        putLong("checkOut", checkOut.toEpochDay())
        putInt("rooms", rooms)
    }
}

internal fun Bundle.toRoomSearchParameters(): RoomSearchParameter {
    val now = LocalDate.now()
    val placeId = getString("placeId","")
    val checkInEpochDay = getLong("checkIn")
    val checkOutEpochDay = getLong("checkOut")
    val rooms = getInt("rooms", 1)
    return RoomSearchParameter(
        placeId = placeId,
        checkIn = if (checkInEpochDay == 0L) now else LocalDate.ofEpochDay(checkInEpochDay),
        checkOut = if (checkOutEpochDay == 0L) now else LocalDate.ofEpochDay(checkOutEpochDay),
        rooms = rooms
    )
}