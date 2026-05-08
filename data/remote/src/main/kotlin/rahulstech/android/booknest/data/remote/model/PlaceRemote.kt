package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PlaceRemote(
    val cityId: String? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val description: String? = null
)
