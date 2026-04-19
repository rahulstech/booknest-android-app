package rahulstech.android.booknest.ui.screen.verifyotp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.LoadingOverlay
import rahulstech.android.booknest.ui.theme.BookNestTheme

// ── Color tokens (mirrors BookNestTheme palette) ──────────────────────────────
private val GradientTop    = Color(0xFF1A8FB5)   // light sky-blue
private val GradientBottom = Color( 0xFF64C8F0)   // deeper teal-blue
private val ButtonDark      @Composable get() = MaterialTheme.colorScheme.primary
private val ButtonLight     @Composable get() = MaterialTheme.colorScheme.primaryContainer
private val LightButtonText @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun VerifyOtpScreen(
    onVerifyOtp     : (otp: String) -> Unit = {},
    onResendOtp     : () -> Unit = {},
    onEditPhone     : () -> Unit = {}
) {
    var otpValue        by remember { mutableStateOf("") }
    var isVerifying     by remember { mutableStateOf(false) }
    var timerSeconds    by remember { mutableIntStateOf(41) }
    var canResend       by remember { mutableStateOf(false) }
    val scope           = rememberCoroutineScope()
    val focusRequester  = remember { FocusRequester() }

    // Countdown timer
    LaunchedEffect(Unit) {
        while (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        }
        canResend = true
    }

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
                        elevation   = 12.dp,
                        shape       = MaterialTheme.shapes.small,
                        ambientColor = Color(0x40000000),
                        spotColor   = Color(0x40000000)
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
                        focusRequester = focusRequester,
                        onValueChange  = { if (it.length <= 6) otpValue = it },
                        enabled        = !isVerifying
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // ── Verify OTP ────────────────────────────────────────────
                    OtpActionButton(
                        text      = stringResource(R.string.verify_otp_btn_verify),
                        enabled   = otpValue.length == 6 && !isVerifying,
                        style     = OtpButtonStyle.LIGHT,
                        onClick   = {
                            scope.launch {
                                isVerifying = true
                                delay(2000L)
                                isVerifying = false
                                onVerifyOtp(otpValue)
                            }
                        }
                    )

                    // ── Resend OTP ────────────────────────────────────────────
                    OtpActionButton(
                        text    = if (canResend) stringResource(R.string.verify_otp_btn_resend)
                        else           stringResource(R.string.verify_otp_btn_resend_timer, formatTimer(timerSeconds)),
                        enabled = canResend && !isVerifying,
                        style   = OtpButtonStyle.LIGHT,
                        onClick = {
                            scope.launch {
                                canResend    = false
                                timerSeconds = 41
                                otpValue     = ""
                                onResendOtp()
                                while (timerSeconds > 0) {
                                    delay(1000L)
                                    timerSeconds--
                                }
                                canResend = true
                            }
                        }
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

    // Auto-focus the hidden text field on launch
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

// ── OTP input row ─────────────────────────────────────────────────────────────
@Composable
private fun OtpInputRow(
    otpValue       : String,
    focusRequester : FocusRequester,
    onValueChange  : (String) -> Unit,
    enabled        : Boolean,
    otpLength      : Int = 6
) {
    Box(contentAlignment = Alignment.Center) {
        // Hidden real text-field that captures keyboard input
        BasicTextField(
            value         = otpValue,
            onValueChange = { new ->
                if (new.all { it.isDigit() } && new.length <= otpLength)
                    onValueChange(new)
            },
            modifier      = Modifier
                .size(1.dp)          // invisible but focused
                .focusRequester(focusRequester),
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
                        .size(width = 44.dp, height = 50.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(
                            width = if (isFilled) 2.dp else 1.5.dp,
                            color = if (isFilled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.small
                        ),
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
        VerifyOtpScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Filled OTP")
@Composable
private fun VerifyOtpFilledPreview() {
    BookNestTheme {
        VerifyOtpScreen()
    }
}
