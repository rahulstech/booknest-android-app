package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class RoomBookingRemote(
    val roomIds: List<String>? = null
)
