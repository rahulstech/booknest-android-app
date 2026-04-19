package rahulstech.android.booknest.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rahulstech.android.booknest.ui.theme.BookNestTheme

// ── Brand colors ──────────────────────────────────────────────────────────────
private val GradientTop    = Color(0xFF1A8FB5)   // light sky-blue
private val GradientBottom = Color( 0xFF64C8F0)   // deeper teal-blue

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun SignUpScreen(
    onSendOtp: (name: String, email: String, mobile: String) -> Unit,
    isLoading: Boolean = false
) {
    var fullName     by remember { mutableStateOf("") }
    var email        by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }

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

            // ── Title ────────────────────────────────────────────────────────
            Text(
                text = "Sign Up",
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
                shape = RoundedCornerShape(20.dp),
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
                        value        = fullName,
                        onValueChange = { fullName = it },
                        label        = "Full Name",
                        icon         = Icons.Outlined.AccountCircle,
                        keyboardType = KeyboardType.Text,
                        enabled      = !isLoading
                    )

                    // Email
                    SignUpTextField(
                        value        = email,
                        onValueChange = { email = it },
                        label        = "Email",
                        icon         = Icons.Outlined.Email,
                        keyboardType = KeyboardType.Email,
                        enabled      = !isLoading
                    )

                    // Mobile Number
                    SignUpTextField(
                        value        = mobileNumber,
                        onValueChange = { if (it.length <= 10) mobileNumber = it },
                        label        = "Mobile Number",
                        icon         = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        enabled      = !isLoading
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Send OTP Button
                    SendOtpButton(
                        isLoading = isLoading,
                        enabled   = fullName.isNotBlank() &&
                                email.isNotBlank() &&
                                mobileNumber.length == 10,
                        onClick   = { onSendOtp(fullName, email, mobileNumber) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // ── Full Screen Loading Overlay ───────────────────────────────────────
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f))
                    .pointerInput(Unit) { /* Block interactions */ },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
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
    enabled       : Boolean
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
            text       = "Send OTP",
            fontSize   = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@PreviewScreenSizes()
@Composable
fun SignUpScreenPreview() {
    var isLoading    by remember { mutableStateOf(false) }
    val scope        = rememberCoroutineScope()

    BookNestTheme {
        SignUpScreen(
            onSendOtp = { _,_,_ ->
                scope.launch {
                    isLoading = true
                    delay(2500) // fake delay
                    isLoading = false
                }
            },
            isLoading = isLoading
        )
    }
}
