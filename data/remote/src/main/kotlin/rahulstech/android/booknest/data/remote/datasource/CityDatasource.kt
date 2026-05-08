package rahulstech.android.booknest.data.remote.datasource

import rahulstech.android.booknest.data.remote.model.CityRemote
import rahulstech.android.booknest.data.remote.model.PlaceRemote

interface CityDatasource {
    suspend fun getCityNames(): List<CityRemote>
    suspend fun getPlace(placeId: String): PlaceRemote?
}
