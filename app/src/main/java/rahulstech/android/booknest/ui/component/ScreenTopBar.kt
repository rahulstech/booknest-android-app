package rahulstech.android.booknest.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.theme.BookNestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    title: String,
    showNavUp: Boolean = false,
    onNavUp: (()-> Unit)? = null,
    showLogoutAction: Boolean = false,
    onLogout: (()->Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        navigationIcon = {
            if (showNavUp) {
                IconButton(onClick = { onNavUp?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_up)
                    )
                }
            }
        },
        actions = {
            if (showLogoutAction) {
                TopLogoutAction(onLogout = { onLogout?.invoke() })
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenTopBarPreview() {
    BookNestTheme {
        ScreenTopBar(
            title = "Title",
            showNavUp = true,
            showLogoutAction = true
        )
    }
}