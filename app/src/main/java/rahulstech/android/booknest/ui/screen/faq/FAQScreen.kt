package rahulstech.android.booknest.ui.screen.faq

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rahulstech.android.booknest.R
import rahulstech.android.booknest.ui.component.ScreenTopBar
import rahulstech.android.booknest.ui.theme.BookNestTheme

@Composable
fun FAQRoute(
    onLogout: () -> Unit
) {
    FAQScreen(onLogout = onLogout)
}

@Composable
fun FAQScreen(
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                title = stringResource(R.string.faq_title),
                showLogoutAction = true,
                onLogout = onLogout
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Image
            Image(
                painter = painterResource(R.drawable.faq_hero),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            FAQItem(
                question = stringResource(R.string.faq_question_1),
                answer = stringResource(R.string.faq_answer_1)
            )

            Spacer(modifier = Modifier.height(20.dp))

            FAQItem(
                question = stringResource(R.string.faq_question_2),
                answer = stringResource(R.string.faq_answer_2)
            )

            Spacer(modifier = Modifier.height(20.dp))

            FAQItem(
                question = stringResource(R.string.faq_question_3),
                answer = stringResource(R.string.faq_answer_3)
            )
        }
    }
}

@Composable
private fun FAQItem(question: String, answer: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                lineHeight = 24.sp
            ),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FAQScreenPreview() {
    BookNestTheme {
        FAQScreen(onLogout = {})
    }
}
