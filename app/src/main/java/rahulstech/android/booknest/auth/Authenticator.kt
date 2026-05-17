package rahulstech.android.booknest.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import rahulstech.android.booknest.data.model.UserDetails
import java.util.concurrent.TimeUnit

sealed interface PhoneAuthState {

    object Idle: PhoneAuthState

    object SendingCode: PhoneAuthState

    object CodeSent: PhoneAuthState

    object Success: PhoneAuthState

    data class Error(val cause: Throwable): PhoneAuthState

}

class Authenticator private constructor() {

    companion object {
        private const val TAG = "Authenticator"

        val instance by lazy { Authenticator() }
    }

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    init {
        auth.currentUser?.let { firebaseUser ->
            database.child(firebaseUser.uid).get()
                .addOnSuccessListener { snapshot ->
                    currentUser.value = snapshot.getValue(UserDetails::class.java)
                }
        }
    }

    private var verificationId: String? = null
    private var otpResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var pendingUserDetails: UserDetails? = null

    var authState = MutableStateFlow<PhoneAuthState>(PhoneAuthState.Idle)
        private set

    private val verificationCallback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credentials: PhoneAuthCredential) {
           // triggered when firebase automatically detects the OTP
            signInWithCredential(credentials)
        }

        override fun onVerificationFailed(error: FirebaseException) {
            Log.d(TAG, "verification error ", error)
            authState.value = PhoneAuthState.Error(error)
        }

        override fun onCodeSent(vid: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d(TAG, "code sent")
            verificationId = vid
            otpResendToken = token
            authState.value = PhoneAuthState.CodeSent
        }
    }

    var currentUser = mutableStateOf<UserDetails?>(null)
        private set

    fun login(phone: String, userDetails: UserDetails? = null) {
        pendingUserDetails = userDetails
        authState.value = PhoneAuthState.SendingCode

        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phone)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setCallbacks(verificationCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOtp() {
        otpResendToken?.let { token ->
            val options = PhoneAuthOptions.newBuilder()
                .setTimeout(30L, TimeUnit.SECONDS)
                .setCallbacks(verificationCallback)
                .setForceResendingToken(token)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun verifyOtp(otp: String) {
        verificationId?.let { id ->
            val credential = PhoneAuthProvider.getCredential(id, otp)
            signInWithCredential(credential)
        }
    }

    fun logout() {
        auth.signOut()
        currentUser.value = null
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG, "sign in successful")
                val firebaseUser = authResult.user ?: return@addOnSuccessListener
                val details = pendingUserDetails
                if (details != null) {
                    database.child(firebaseUser.uid).setValue(details)
                        .addOnSuccessListener {
                            currentUser.value = details
                            authState.value = PhoneAuthState.Success
                            pendingUserDetails = null
                        }
                        .addOnFailureListener { error ->
                            Log.e(TAG, "failed to save user details", error)
                            authState.value = PhoneAuthState.Error(error)
                        }
                } else {
                    database.child(firebaseUser.uid).get()
                        .addOnSuccessListener { snapshot ->
                            val user = snapshot.getValue(UserDetails::class.java)
                            currentUser.value = user
                            authState.value = PhoneAuthState.Success
                        }
                        .addOnFailureListener { error ->
                            Log.e(TAG, "failed to fetch user details", error)
                            authState.value = PhoneAuthState.Error(error)
                        }
                }
            }
            .addOnFailureListener { error ->
                Log.e(TAG, "sign in failed", error)
                authState.value = PhoneAuthState.Error(error)
            }
    }
}