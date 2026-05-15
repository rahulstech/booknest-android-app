package rahulstech.android.booknest.ui.screen.selectroom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahulstech.android.booknest.data.model.HotelDetails
import rahulstech.android.booknest.data.model.Resource
import rahulstech.android.booknest.data.remote.HotelRepository

class SelectRoomViewModel: ViewModel() {

    private val repo = HotelRepository.instance

    var hotelResource = mutableStateOf<Resource<HotelDetails?>>(Resource.Idle)
        private set

    private var oldHotelId: String = ""

    fun findHotel(hotelId: String) {
        if (oldHotelId == hotelId) {
            return
        }
        oldHotelId = hotelId
        viewModelScope.launch {
            repo.findHotelById(hotelId)
                .collectLatest { resource ->
                    hotelResource.value = resource
                }
        }
    }
}