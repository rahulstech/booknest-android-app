package rahulstech.android.booknest.data.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import rahulstech.android.booknest.data.model.Amenity
import rahulstech.android.booknest.data.model.FindHotelItem
import rahulstech.android.booknest.data.model.HotelDetails
import rahulstech.android.booknest.data.model.Resource
import rahulstech.android.booknest.data.model.RoomDetails

internal fun DataSnapshot.toFindHotelItem(): FindHotelItem? {
    val id = key ?: return null
    val name = child("name").getValue(String::class.java) ?: return null
    val place = child("place").getValue(String::class.java) ?: return null
    val imageUrl = child("imageUrl").getValue(String::class.java) ?: return null
    val priceMin = child("priceMin").getValue(Int::class.java) ?: return null
    val priceMax = child("priceMax").getValue(Int::class.java) ?: return null

    return FindHotelItem(
        id = id,
        hotelName = name,
        cityName = place,
        priceMin = priceMin,
        priceMax = priceMax,
        isSoldOut = false,
        imageUrl = imageUrl,
    )
}

internal fun DataSnapshot.toAmenity(): Amenity {
    val value = key ?: throw NullPointerException("null amenity")
    return Amenity.from(value)
}

internal fun DataSnapshot.toRoomDetails(): RoomDetails? {
    val id = key ?: return null
    val type = child("type").getValue(String::class.java) ?: return null
    val pricePerNight = child("price").getValue(Int::class.java) ?: return null
    val imageUrl = child("imageUrl").getValue(String::class.java) ?: return null

    return RoomDetails(
        id = id,
        roomType = type,
        pricePerDay = pricePerNight,
        photoUrl = imageUrl
    )
}

internal fun DataSnapshot.toHotelDetails(): HotelDetails? {
    val id = key ?: return null
    val place = child("place").getValue(String::class.java) ?: return null
    val name = child("name").getValue(String::class.java) ?: return null
    val imageUrl = child("imageUrl").getValue(String::class.java) ?: return null
    val rating = child("rating").getValue(Float::class.java) ?: return null
    val about = child("about").getValue(String::class.java) ?: return null
    val amenities = child("amenities").children.mapNotNull { it.toAmenity() }
    val rules = child("rules").children.mapNotNull { child ->
        val rule = child.getValue(String::class.java) ?: ""
        rule
    }
    val rooms = child("rooms").children.mapNotNull { child -> child.toRoomDetails() }

    return HotelDetails(
        id = id,
        name = name,
        location = place,
        heroPhotoUrl = imageUrl,
        stars = rating,
        aboutTheHotel = about,
        amenities = amenities,
        rulesAndInformation = rules,
        rooms = rooms
    )
}




class HotelRepository private constructor() {

    companion object  {
        private const val TAG = "HotelRepository"

        val instance by lazy { HotelRepository() }
    }

    private val db = FirebaseDatabase.getInstance()
    private val placeHotelsRef = db.getReference("place_hotels")
    private val hotelsRef = db.getReference("hotels")

    fun getHotelsAtPlace(placeId: String): Flow<Resource<List<FindHotelItem>>> = flow {
        Log.d(TAG, "hotels of place $placeId")

        emit(Resource.Loading)
        try {
            val snapshot = placeHotelsRef.child(placeId).get().await()
            val hotels = snapshot.children.mapNotNull { child -> child.toFindHotelItem() }

            emit(Resource.Success(hotels))
        }
        catch (cause: Throwable) {
            emit(Resource.Error(cause))
        }
    }.flowOn(Dispatchers.IO)


    fun findHotelById(hotelId: String): Flow<Resource<HotelDetails?>> = flow {
        Log.d(TAG,"loading hotel with id $hotelId")
        emit(Resource.Loading)
        try {
            val snapshot = hotelsRef.child(hotelId).get().await()
            val hotel = snapshot.toHotelDetails()

            emit(Resource.Success(hotel))
        }
        catch (cause: Throwable) {
            emit(Resource.Error(cause))
        }
    }.flowOn(Dispatchers.IO)
}