package rahulstech.android.booknest.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.LoadingOverlay
import rahulstech.android.booknest.ui.theme.BookNestTheme

import rahulstech.android.booknest.ui.theme.brandGradientBottom
import rahulstech.android.booknest.ui.theme.brandGradientTop

@Composable
fun SignupRoute(
    onOtpSent: ()-> Unit,
    viewModel: AuthViewModel,
) {
    val uiState by viewModel.signUpUiState

    LaunchedEffect(uiState.isLoading, uiState.isCodeSent) {
        if (!uiState.isLoading && uiState.isCodeSent) {
            onOtpSent()
        }
    }

    SignUpScreen(
        uiState = uiState,
        onChangeName = viewModel::updateName,
        onChangeEmail = viewModel::updateEmail,
        onChangePhone = viewModel::updatePhone,
        onSendOtp = {
            viewModel.signup()
        }
    )
}


// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun SignUpScreen(
    uiState: SignUpUIState,
    onChangeName: (String)-> Unit,
    onChangeEmail: (String)-> Unit,
    onChangePhone: (String)-> Unit,
    onSendOtp: ()-> Unit,
) {
    Box(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(brandGradientTop, brandGradientBottom)
            )),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            // ── Title ────────────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.signup_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Card ──────────────────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = MaterialTheme.shapes.small,
                        ambientColor = Color(0x40000000),
                        spotColor   = Color(0x40000000)
                    ),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // Full Name
                    SignUpTextField(
                        value        = uiState.name,
                        onValueChange = onChangeName,
                        label        = stringResource(R.string.signup_label_full_name),
                        icon         = Icons.Outlined.AccountCircle,
                        keyboardType = KeyboardType.Text,
                        enabled      = !uiState.isLoading,
                        error        = uiState.nameError
                    )

                    // Email
                    SignUpTextField(
                        value        = uiState.email,
                        onValueChange = onChangeEmail,
                        label        = stringResource(R.string.signup_label_email),
                        icon         = Icons.Outlined.Email,
                        keyboardType = KeyboardType.Email,
                        enabled      = !uiState.isLoading,
                        error        = uiState.emailError
                    )

                    // Mobile Number
                    SignUpTextField(
                        value        = uiState.phone,
                        onValueChange = onChangePhone,
                        label        = stringResource(R.string.signup_label_mobile_number),
                        icon         = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        enabled      = !uiState.isLoading,
                        error        = uiState.phoneError
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Send OTP Button
                    SendOtpButton(
                        isLoading = uiState.isLoading,
                        enabled   = !uiState.isLoading,
                        onClick   = onSendOtp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // ── Full Screen Loading Overlay ───────────────────────────────────────
        LoadingOverlay(
            show = uiState.isLoading,
            message = stringResource(R.string.signup_loading)
        )
    }
}

// ── Reusable outlined text-field ──────────────────────────────────────────────
@Composable
private fun SignUpTextField(
    value         : String,
    onValueChange : (String) -> Unit,
    label         : String,
    icon          : ImageVector,
    keyboardType  : KeyboardType,
    enabled       : Boolean,
    error         : String? = null
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        modifier      = Modifier.fillMaxWidth(),
        enabled       = enabled,
        label         = {
            Text(
                text      = label,
                fontSize  = 13.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector        = icon,
                contentDescription = label,
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
        singleLine      = true,
        shape           = MaterialTheme.shapes.extraSmall,
        isError         = error != null,
        supportingText  = error?.let {
            {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    )
}

// ── Send OTP button ───────────────────────────────────────────────────────────
@Composable
private fun SendOtpButton(
    isLoading : Boolean,
    enabled   : Boolean,
    onClick   : () -> Unit
) {
    Button(
        onClick  = { if (!isLoading && enabled) onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape    = MaterialTheme.shapes.extraSmall,
        enabled  = enabled && !isLoading,
    ) {
        Text(
            text       = stringResource(R.string.signup_btn_send_otp),
            fontSize   = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@PreviewScreenSizes
@Composable
fun SignUpScreenPreview() {
    var uiState    by remember { mutableStateOf(SignUpUIState()) }
    val coroutineScope        = rememberCoroutineScope()

    BookNestTheme {
        SignUpScreen(
            uiState = uiState,
            onChangeName = {},
            onChangeEmail = {},
            onChangePhone = {},
            onSendOtp = {
                coroutineScope.launch {
                    uiState = uiState.copy(isLoading = true)
                    delay(3000)
                    uiState = uiState.copy(isLoading = false)
                }
            }
        )
    }
}
