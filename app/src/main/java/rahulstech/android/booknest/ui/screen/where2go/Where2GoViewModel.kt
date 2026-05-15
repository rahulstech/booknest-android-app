package rahulstech.android.booknest.ui.screen.where2go

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahulstech.android.booknest.data.model.Place
import rahulstech.android.booknest.data.remote.PlaceRepository

data class Where2GoUIState(
    val isLoading: Boolean = false,
    val allPlaces: List<Place> = emptyList(),
    val error: Throwable? = null
)

class Where2GoViewModel: ViewModel() {

    private val repo = PlaceRepository.instance

    var uiState = mutableStateOf(Where2GoUIState(isLoading = true))

    fun loadPlaces() {
        viewModelScope.launch {
            repo.getAllPlaces()
                .catch { error ->
                    uiState.value = Where2GoUIState(
                        isLoading = false,
                        error = error
                    )
                }
                .collectLatest { places ->
                    uiState.value = Where2GoUIState(
                        isLoading = false,
                        allPlaces = places
                    )
                }
        }
    }
}