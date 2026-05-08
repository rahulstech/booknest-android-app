package rahulstech.android.booknest.data.remote.datasource.impl

import rahulstech.android.booknest.data.remote.model.HotelRemote
import rahulstech.android.booknest.data.remote.model.RoomRemote
import rahulstech.android.booknest.data.remote.datasource.HotelDatasource

class HotelDatasourceImpl : HotelDatasource {
    override suspend fun getHotel(cityId: String): List<HotelRemote> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoomsOfHotel(hotelId: String): List<RoomRemote> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoomAvailablity(roomId: String, dateStart: Long, dateEnd: Long): Boolean {
        TODO("Not yet implemented")
    }
}
