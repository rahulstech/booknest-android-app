package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class HotelRemote(
    val cityId: String? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val rating: Double? = null,
    val about: String? = null,
    val priceMin: Int? = null,
    val priceMax: Int? = null,
    val amenities: AmenitiesRemote? = null,
    val rules: RulesRemote? = null
)

@IgnoreExtraProperties
data class AmenitiesRemote(
    val gym: Boolean? = null,
    val freeParking: Boolean? = null,
    val restaurant: Boolean? = null
)

@IgnoreExtraProperties
data class RulesRemote(
    val checkIn: String? = null,
    val checkOut: String? = null,
    val petsAllowed: Boolean? = null,
    val outsideFoodAllowed: Boolean? = null,
    val acceptedIds: String? = null
)
