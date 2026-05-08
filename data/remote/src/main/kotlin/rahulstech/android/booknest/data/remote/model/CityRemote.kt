package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CityRemote(
    val name: String? = null,
    val imageUrl: String? = null
)
