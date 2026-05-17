package rahulstech.android.booknest.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.LoadingOverlay
import rahulstech.android.booknest.ui.theme.BookNestTheme

// ── Color tokens (mirrors BookNestTheme palette) ──────────────────────────────
private val GradientTop    = Color(0xFF1A8FB5)   // light sky-blue
private val GradientBottom = Color( 0xFF64C8F0)   // deeper teal-blue
private val ButtonDark      @Composable get() = MaterialTheme.colorScheme.primary
private val ButtonLight     @Composable get() = MaterialTheme.colorScheme.primaryContainer
private val LightButtonText @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer



@Composable
fun VerifyOtpRoute(
    onExit: ()-> Unit,
    onNavigateToMain: ()-> Unit,
    viewModel: AuthViewModel
) {
    val uiState by viewModel.signUpUiState

    if (!uiState.isLoading && uiState.isSuccess) {
        onNavigateToMain()
    }
    else {
        VerifyOtpScreen(
            isVerifying = uiState.isLoading,
            otpResendAfterSeconds = viewModel.otpResendAfterSeconds,
            onVerifyOtp = { otp ->
                viewModel.verifyOtp(otp)
            },
            onResendOtp = {
                viewModel.resendOtp()
            },
            onEditPhone = {
                viewModel.resetCodeSentState()
                onExit()
            }
        )
    }
}

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun VerifyOtpScreen(
    isVerifying     : Boolean,
    otpResendAfterSeconds: Int? = null,
    onVerifyOtp     : (otp: String) -> Unit,
    onResendOtp     : () -> Unit,
    onEditPhone     : () -> Unit
) {
    var otpValue        by remember { mutableStateOf("") }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(GradientTop, GradientBottom)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // ── Page Title ────────────────────────────────────────────────────
            Text(
                text       = stringResource(R.string.signup_title),
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onPrimary
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
                        spotColor = Color(0x40000000)
                    ),
                shape  = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {

                    // ── Card header ───────────────────────────────────────────
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text       = stringResource(R.string.verify_otp_title),
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF1A2E38)
                        )
                        Text(
                            text     = stringResource(R.string.verify_otp_subtitle),
                            fontSize = 13.sp,
                            color    = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }

                    // ── OTP Boxes ─────────────────────────────────────────────
                    OtpInputRow(
                        otpValue       = otpValue,
                        onValueChange  = { if (it.length <= 6) otpValue = it },
                        enabled        = !isVerifying
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // ── Verify OTP ────────────────────────────────────────────
                    OtpActionButton(
                        text      = stringResource(R.string.verify_otp_btn_verify),
                        enabled   = otpValue.length == 6 && !isVerifying,
                        style     = OtpButtonStyle.LIGHT,
                        onClick   = { onVerifyOtp(otpValue) }
                    )

                    // ── Resend OTP ────────────────────────────────────────────
                    OtpActionButton(
                        text    = otpResendAfterSeconds?.let { seconds ->
                            stringResource(R.string.verify_otp_btn_resend_timer, formatTimer(seconds))
                        } ?: stringResource(R.string.verify_otp_btn_resend),
                        enabled = otpResendAfterSeconds == null && !isVerifying,
                        style   = if (otpResendAfterSeconds == null) OtpButtonStyle.DARK else OtpButtonStyle.LIGHT,
                        onClick = onResendOtp
                    )

                    // ── Edit Phone Number ─────────────────────────────────────
                    OtpActionButton(
                        text    = stringResource(R.string.verify_otp_btn_edit_phone),
                        enabled = !isVerifying,
                        style   = OtpButtonStyle.DARK,
                        onClick = onEditPhone
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // ── Full Screen Loading Overlay ───────────────────────────────────────
        LoadingOverlay(
            show = isVerifying,
            message = stringResource(R.string.verify_otp_loading)
        )
    }
}

// ── OTP input row ─────────────────────────────────────────────────────────────
@Composable
private fun OtpInputRow(
    otpValue       : String,
    onValueChange  : (String) -> Unit,
    enabled        : Boolean,
    otpLength      : Int = 6
) {
    val softKeyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Box(contentAlignment = Alignment.Center) {
        // Hidden real text-field that captures keyboard input
        BasicTextField(
            value         = otpValue,
            onValueChange = { new ->
                if (new.all { it.isDigit() } && new.length <= otpLength)
                    onValueChange(new)
            },
            modifier      = Modifier
                .size(1.dp)
                .focusRequester(focusRequester),          // invisible but focused
            enabled       = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            cursorBrush   = SolidColor(Color.Transparent)
        )

        // Visual boxes
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            repeat(otpLength) { index ->
                val char    = otpValue.getOrNull(index)
                val isFilled = char != null

                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = if (isFilled) 2.dp else 1.5.dp,
                            color = if (isFilled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.small
                        )
                        .clickable(onClick = {
                            focusRequester.requestFocus()
                            softKeyboard?.show()
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = char?.toString() ?: "",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign  = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ── Button style enum ─────────────────────────────────────────────────────────
private enum class OtpButtonStyle { LIGHT, DARK }

// ── Generic action button ─────────────────────────────────────────────────────
@Composable
private fun OtpActionButton(
    text      : String,
    enabled   : Boolean,
    style     : OtpButtonStyle,
    onClick   : () -> Unit
) {
    val containerColor = when {
        !enabled && style == OtpButtonStyle.DARK -> ButtonDark.copy(alpha = 0.5f)
        !enabled                                 -> ButtonLight.copy(alpha = 0.5f)
        style == OtpButtonStyle.DARK             -> ButtonDark
        else                                     -> ButtonLight
    }
    val contentColor = if (style == OtpButtonStyle.DARK) Color.White else LightButtonText

    Button(
        onClick   = { if (enabled) onClick() },
        modifier  = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape     = MaterialTheme.shapes.small,
        enabled   = enabled,
        colors    = ButtonDefaults.buttonColors(
            containerColor         = containerColor,
            disabledContainerColor = containerColor,
            contentColor           = contentColor,
            disabledContentColor   = contentColor.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation  = if (style == OtpButtonStyle.DARK) 4.dp else 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text       = text,
            fontSize   = 15.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp
        )
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
private fun formatTimer(seconds: Int): String {
    val mm = seconds / 60
    val ss = seconds % 60
    return "%02d:%02d".format(mm, ss)
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(showBackground = true, showSystemUi = true, name = "Empty OTP")
@Composable
private fun VerifyOtpEmptyPreview() {
    BookNestTheme {
        VerifyOtpScreen(
            isVerifying = false,
            onVerifyOtp = {},
            onResendOtp = {},
            onEditPhone = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Filled OTP")
@Composable
private fun VerifyOtpFilledPreview() {
    BookNestTheme {
        VerifyOtpScreen(
            isVerifying = false,
            onVerifyOtp = {},
            onResendOtp = {},
            onEditPhone = {}
        )
    }
}
