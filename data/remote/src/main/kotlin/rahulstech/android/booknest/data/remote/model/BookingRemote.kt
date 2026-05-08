package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BookingRemote(
    val userId: String? = null,
    val hotelId: String? = null,
    val cityId: String? = null,
    val checkIn: String? = null,
    val checkOut: String? = null,
    val numDays: Int? = null,
    val totalPrice: Int? = null,
    val roomIds: Map<String, String>? = null
)
