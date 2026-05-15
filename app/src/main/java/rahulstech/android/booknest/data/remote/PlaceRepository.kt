package rahulstech.android.booknest.data.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.model.PlaceName


internal fun DataSnapshot.toPlaceName(): PlaceName {
    val id = key ?: throw IllegalStateException("place id is null")
    val name = child("name").getValue(String::class.java) ?: throw IllegalStateException("place $id name is null")
    return PlaceName(id,name)
}

internal fun DataSnapshot.toPlace(): Place? {
    val id = key ?: throw IllegalStateException("place id is null")
    val name = child("name").getValue(String::class.java) ?: return null
    val imageUrl = child("imageUrl").getValue(String::class.java) ?: return null
    val description = child("description").getValue(String::class.java) ?: return null
    return Place(id,name,imageUrl,description)
}


class PlaceRepository private constructor() {

    companion object {

        val instance by lazy { PlaceRepository() }
    }

    private val _db = FirebaseDatabase.getInstance()
    private val _places = _db.getReference("places")

    fun getAllPlaces(): Flow<List<Place>> = flow {
        val snapshot = _places
            .get()
            .await()
        val places = snapshot.children.mapNotNull { it.toPlace() }

        emit(places)
    }.flowOn(Dispatchers.IO)

    fun getAllPlaceNames(): Flow<List<PlaceName>> = flow {
        val snapshot = _places
            .get()
            .await()
        val names = snapshot.children.mapNotNull { it.toPlaceName() }

        emit(names)
    }.flowOn(Dispatchers.IO)

    fun getBestPlaces(): Flow<List<Place>> = flow {
        val snapshot = _places
            .limitToLast(3)
            .get()
            .await()
        val bestPlaces = snapshot.children.mapNotNull { it.toPlace() }

        emit(bestPlaces)
    }.flowOn(Dispatchers.IO)

    suspend fun findPlaceById(id: String): Place? {
        val snapshot = _places.child(id).get().await()
        val place = snapshot.toPlace()
        return place
    }
}