package rahulstech.android.booknest.data.remote.datasource.impl

import rahulstech.android.booknest.data.remote.model.CityRemote
import rahulstech.android.booknest.data.remote.model.PlaceRemote
import rahulstech.android.booknest.data.remote.datasource.CityDatasource

class CityDatasourceImpl : CityDatasource {
    override suspend fun getCityNames(): List<CityRemote> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlace(placeId: String): PlaceRemote? {
        TODO("Not yet implemented")
    }
}
