package rahulstech.android.booknest.ui.screen.place

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.remote.PlaceRepository

data class PlaceUIState(
    val isLoading: Boolean = false,
    val place: Place? = null,
    val isSuccessful: Boolean = false,
    val error: Throwable? = null
)


class PlaceViewModel: ViewModel() {

    companion object {
        private const val TAG = "PlaceViewHolder"
    }

    private val repo = PlaceRepository.instance

    var uiState = mutableStateOf(PlaceUIState(isLoading = true))

    private var oldPlaceId: String = ""

    fun findPlace(placeId: String) {
        if (placeId == oldPlaceId) {
            return
        }
        oldPlaceId = placeId
        Log.d(TAG, "loading place $placeId")
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = PlaceUIState(isLoading = true)
            try {
                val place = repo.findPlaceById(placeId)
                Log.d(TAG,"place[$placeId] = $place")

                if (null == place) {
                    uiState.value = PlaceUIState(isLoading = false, isSuccessful = false)
                }
                else {
                    uiState.value = PlaceUIState(isLoading = false, isSuccessful = true, place = place)
                }
            } catch (error: Throwable) {
                Log.e(TAG, "fail to find place with id $placeId", error)
                uiState.value = PlaceUIState(isLoading = false, isSuccessful = false, error = error)
            }
        }
    }
}

