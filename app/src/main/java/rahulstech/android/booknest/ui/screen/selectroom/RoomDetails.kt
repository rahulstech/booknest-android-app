package rahulstech.android.booknest.ui.screen.selectroom

data class RoomDetails(
    val id: String,
    val roomType: String,
    val pricePerDay: Int,      // in INR
    val photoUrl: String
)