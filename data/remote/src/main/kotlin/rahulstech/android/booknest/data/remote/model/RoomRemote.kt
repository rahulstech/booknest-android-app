package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RoomRemote(
    val hotelId: String? = null,
    val name: String? = null,
    val price: Int? = null,
    val imageUrl: String? = null,
    val isAvailable: Boolean? = null
)
