package rahulstech.android.booknest.data.remote.datasource

import rahulstech.android.booknest.data.remote.model.BookingRemote

interface BookingDatasource {
    suspend fun addBooking(booking: BookingRemote)
}
