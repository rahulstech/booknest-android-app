package rahulstech.android.booknest.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserRemote(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null
)
