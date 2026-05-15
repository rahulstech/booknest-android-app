package rahulstech.android.booknest.ui.screen.selecthotel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahulstech.android.booknest.data.model.FindHotelItem
import rahulstech.android.booknest.data.model.Resource
import rahulstech.android.booknest.data.remote.HotelRepository

class SelectHotelViewModel: ViewModel() {

    private val repo = HotelRepository.instance

    var hotelResource = mutableStateOf<Resource<List<FindHotelItem>>>(Resource.Idle)
        private set

    private var oldPlaceId: String = ""

    fun fetchHotelsAtPlace(placeId: String) {
        if (oldPlaceId == placeId) {
            return
        }
        oldPlaceId = placeId
        viewModelScope.launch {
            repo.getHotelsAtPlace(placeId)
                .collectLatest {
                    hotelResource.value = it
                }
        }
    }
}