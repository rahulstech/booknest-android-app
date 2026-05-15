package rahulstech.android.booknest.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import rahulstech.android.booknest.R
import java.util.Locale

enum class Amenity(
    @param:DrawableRes val iconRes: Int,          // drawable resource id
    @param:StringRes val label: Int
) {
    GYM(R.drawable.ic_gym, R.string.amenity_gym),

    RESTAURANT(R.drawable.ic_restaurant, R.string.amenity_restaurant),

    PARKING(R.drawable.ic_free_parking, R.string.amenity_parking),

    ;

    companion object {
        fun from(value: String): Amenity {
            return when(value.trim().uppercase(Locale.ENGLISH)) {
                "GYM" -> GYM
                "RESTAURANT",
                "RESTAURENT" -> RESTAURANT
                "PARKING" -> PARKING
                else -> throw IllegalStateException("unknow amenity $value")
            }
        }
    }
}