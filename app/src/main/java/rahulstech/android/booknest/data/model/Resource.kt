package rahulstech.android.booknest.data.model

sealed interface Resource<out T> {

    object Idle: Resource<Nothing>

    object Loading: Resource<Nothing>

    data class Success<out T>(val data: T): Resource<T>

    data class Error(val cause: Throwable): Resource<Nothing>
}