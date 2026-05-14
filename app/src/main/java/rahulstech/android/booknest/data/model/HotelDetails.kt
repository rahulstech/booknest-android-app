package rahulstech.android.booknest.data.model

data class HotelDetails(
    val heroPhotoUrl: String,
    val name: String,
    val location: String,
    val stars: Float,
    val aboutTheHotel: String,
    val amenities: List<Amenity>,
    val rulesAndInformation: List<String>
)