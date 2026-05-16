package rahulstech.android.booknest.ui.screen.findroom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.model.PlaceName
import rahulstech.android.booknest.data.remote.PlaceRepository
import rahulstech.android.booknest.util.samplePlaces
import java.time.LocalDate

data class FindRoomUIState(
    val allLocations: List<PlaceName>,
    val bestPlaces: List<Place> = samplePlaces,
    val location: PlaceName? = null,
    val rooms: Int = 1,
    val checkIn: LocalDate = LocalDate.now(),
    val checkOut: LocalDate = LocalDate.now(),
)

class FindRoomViewModel: ViewModel() {

    private val placeRepo = PlaceRepository.instance

    var uiState = mutableStateOf(FindRoomUIState(bestPlaces = emptyList(), allLocations = emptyList()))
        private set

    init {
        viewModelScope.launch {
            placeRepo.getAllPlaceNames().collectLatest { allLocations ->
                uiState.value = uiState.value.copy(
                    allLocations = allLocations
                )
            }
        }

        viewModelScope.launch {
            placeRepo.getBestPlaces().collectLatest { places ->
                uiState.value = uiState.value.copy(
                    bestPlaces = places
                )
            }
        }
    }

    fun updateLocation(location: PlaceName) {
        uiState.value = uiState.value.copy(location = location)
    }

    fun updateRooms(rooms: Int) {
        uiState.value = uiState.value.copy(rooms = rooms)
    }

    fun updateCheckInDate(date: LocalDate) {
        uiState.value = uiState.value.copy(checkIn = date)
    }

    fun updateCheckOutDate(date: LocalDate) {
        uiState.value = uiState.value.copy(checkOut = date)
    }
}

