package rahulstech.android.booknest.ui.screen.hotelbooking

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahulstech.android.booknest.data.model.FindHotelItem
import rahulstech.android.booknest.data.model.HotelDetails
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.model.PlaceName
import rahulstech.android.booknest.data.model.Resource
import rahulstech.android.booknest.data.remote.HotelRepository
import rahulstech.android.booknest.data.remote.PlaceRepository
import rahulstech.android.booknest.ui.screen.hotelbooking.RoomSearchParameter
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

class BookHotelViewModel: ViewModel() {

    private val placeRepo = PlaceRepository.instance
    private val hotelRepo = HotelRepository.instance

    var roomSearchParameter: RoomSearchParameter = RoomSearchParameter()

    var hotelDetailsResource = mutableStateOf<Resource<HotelDetails?>>(Resource.Idle)
        private set

    var selectedRoomIds = mutableStateSetOf<String>()
        private set

    var uiState = mutableStateOf(FindRoomUIState(bestPlaces = emptyList(), allLocations = emptyList()))
        private set

    var hotelsResource = mutableStateOf<Resource<List<FindHotelItem>>>(Resource.Idle)
        private set

    private var oldHotelId: String = ""

    private var oldPlaceId: String = ""

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

    fun fetchHotelsAtPlace(placeId: String) {
        if (oldPlaceId == placeId) {
            return
        }
        oldPlaceId = placeId
        viewModelScope.launch {
            hotelRepo.getHotelsAtPlace(placeId)
                .collectLatest {
                    hotelsResource.value = it
                }
        }
    }

    fun findHotel(hotelId: String) {
        if (oldHotelId == hotelId) {
            return
        }
        oldHotelId = hotelId
        viewModelScope.launch {
            hotelRepo.findHotelById(hotelId)
                .collectLatest { resource ->
                    hotelDetailsResource.value = resource
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

    fun toggleRoomSelection(roomId: String) {
        val selected = roomId in selectedRoomIds
        if (selected) {
            selectedRoomIds.remove(roomId)
        }
        else {
            selectedRoomIds.add(roomId)
        }
    }

    fun unselectRoom(roomId: String) {
        selectedRoomIds.remove(roomId)
    }

















}