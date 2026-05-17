package rahulstech.android.booknest.ui.screen.signup

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import rahulstech.android.booknest.auth.Authenticator
import rahulstech.android.booknest.auth.PhoneAuthState
import rahulstech.android.booknest.data.model.UserDetails

data class SignUpUIState(
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val isLoading: Boolean = false,
    val isCodeSent: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Throwable? = null
) {
    fun toUserDetails(): UserDetails = UserDetails(name = name, email = email, phone = phone)
}

class AuthViewModel : ViewModel() {

    private val auth = Authenticator.instance

    var signUpUiState = mutableStateOf(SignUpUIState())
        private set

    var otpResendAfterSeconds by mutableStateOf<Int?>(null)
        private set

    private var resendTimerJob: Job? = null

    init {
        viewModelScope.launch {
            auth.authState.collectLatest { state ->
                when (state) {
                    is PhoneAuthState.SendingCode -> {
                        updateSignUpState(isLoading = true, isCodeSent = false, isSuccess = false, error = null)
                    }
                    is PhoneAuthState.CodeSent -> {
                        updateSignUpState(isLoading = false, isCodeSent = true)
                        startResendTimer()
                    }
                    is PhoneAuthState.Success -> {
                        updateSignUpState(isLoading = false, isSuccess = true)
                    }
                    is PhoneAuthState.Error -> {
                        updateSignUpState(isLoading = false, isCodeSent = false, isSuccess = false, error = state.cause)
                    }
                    else -> {}
                }
            }
        }
    }

    fun signup() {
        if (!validateForm()) return
        val inputs = signUpUiState.value
        // ensure phone number has country code if required by firebase, 
        // but here we assume Authenticator handles it or phone is already formatted
        auth.login(inputs.phone, inputs.toUserDetails())
    }

    private fun validateForm(): Boolean {
        val current = signUpUiState.value
        val nameError = if (current.name.trim().isEmpty()) "Name is required" else null
        val emailError = when {
            current.email.isEmpty() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(current.email).matches() -> "Invalid email address"
            else -> null
        }
        val phoneError = when {
            current.phone.isEmpty() -> "Phone number is required"
            current.phone.length < 10 -> "Enter a valid 10-digit phone number"
            else -> null
        }

        updateSignUpState(
            nameError = nameError,
            emailError = emailError,
            phoneError = phoneError
        )

        return nameError == null && emailError == null && phoneError == null
    }

    fun verifyOtp(otp: String) {
        if (otp.length < 6) return
        updateSignUpState(isLoading = true, isSuccess = false, error = null)
        auth.verifyOtp(otp)
    }

    fun resendOtp() {
        if (otpResendAfterSeconds == null) {
            auth.resendOtp()
        }
    }

    private fun startResendTimer() {
        resendTimerJob?.cancel()
        resendTimerJob = viewModelScope.launch {
            for (i in 30 downTo 0) {
                otpResendAfterSeconds = i
                delay(1000)
            }
            otpResendAfterSeconds = null
        }
    }

    fun updateName(name: String) {
        updateSignUpState(name = name, nameError = null)
    }

    fun updatePhone(phone: String) {
        // required format +[country-code][phone-number]
        // country code is up to 3 digit long
        // phone number is up to 12 digits long
        // so max allowed characters is 16
        val filtered = phone.filter { it == '+' || it.isDigit() }.take(16)
        updateSignUpState(phone = filtered, phoneError = null)
    }

    fun updateEmail(email: String) {
        updateSignUpState(email = email, emailError = null)
    }

    fun resetCodeSentState() {
        resendTimerJob?.cancel()
        otpResendAfterSeconds = null
        updateSignUpState(isLoading = false, isCodeSent = false, isSuccess = false, error = null)
    }

    private fun updateSignUpState(
        name: String = signUpUiState.value.name,
        nameError: String? = signUpUiState.value.nameError,
        email: String = signUpUiState.value.email,
        emailError: String? = signUpUiState.value.emailError,
        phone: String = signUpUiState.value.phone,
        phoneError: String? = signUpUiState.value.phoneError,
        isLoading: Boolean = signUpUiState.value.isLoading,
        isCodeSent: Boolean = signUpUiState.value.isCodeSent,
        isSuccess: Boolean = signUpUiState.value.isSuccess,
        error: Throwable? = signUpUiState.value.error
    ) {
        signUpUiState.value = signUpUiState.value.copy(
            name = name,
            nameError = nameError,
            email = email,
            emailError = emailError,
            phone = phone,
            phoneError = phoneError,
            isLoading = isLoading,
            isCodeSent = isCodeSent,
            isSuccess = isSuccess,
            error = error
        )
    }
}
