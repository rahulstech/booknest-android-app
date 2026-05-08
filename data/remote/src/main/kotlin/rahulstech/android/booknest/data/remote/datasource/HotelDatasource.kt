package rahulstech.android.booknest.data.remote.datasource

import rahulstech.android.booknest.data.remote.model.HotelRemote
import rahulstech.android.booknest.data.remote.model.RoomRemote

interface HotelDatasource {
    suspend fun getHotel(cityId: String): List<HotelRemote>
    suspend fun getRoomsOfHotel(hotelId: String): List<RoomRemote>
    suspend fun getRoomAvailablity(roomId: String, dateStart: Long, dateEnd: Long): Boolean
}
