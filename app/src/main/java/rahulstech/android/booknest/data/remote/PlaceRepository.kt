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

    fun getAllPlaceNames(): Flow<List<PlaceName>> = flow {
        val snapshot = _db.getReference("places")
            .get()
            .await()
        val names = snapshot.children.mapNotNull { it.toPlaceName() }

        emit(names)
    }.flowOn(Dispatchers.IO)

    fun getBestPlaces(): Flow<List<Place>> = flow {

        val snapshot = _db.getReference("places")
            .limitToLast(3)
            .get()
            .await()
        val places = snapshot.children.mapNotNull { it.toPlace() }

        emit(places)
    }.flowOn(Dispatchers.IO)
}