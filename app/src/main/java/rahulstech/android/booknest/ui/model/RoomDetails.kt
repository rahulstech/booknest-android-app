package rahulstech.android.booknest.ui.model

data class RoomDetails(
    val id: String,
    val roomType: String,
    val pricePerDay: Int,      // in INR
    val photoUrl: String
)