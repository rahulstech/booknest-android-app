package rahulstech.android.booknest.ui.screen.selecthotel

/**
 * Represents a single hotel entry shown in the hotel-selection list.
 *
 * @param hotelName   Display name of the hotel (e.g. "Fairfield Hotel").
 * @param cityName    City where the hotel is located (e.g. "Agra").
 * @param priceMin    Lowest nightly rate in INR.
 * @param priceMax    Highest nightly rate in INR.
 * @param isSoldOut   Whether all rooms are currently booked.
 * @param imageUrl    Remote URL or a local drawable resource path for the hero image.
 */
data class FindHotelItem(
    val id: String,
    val hotelName: String,
    val cityName: String,
    val priceMin: Int = 0,
    val priceMax: Int = 0,
    val isSoldOut: Boolean,
    val imageUrl: String,
)