package rahulstech.android.booknest.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rahulstech.android.booknest.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComposableDialog(
    onDismissRequest: () -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    initialDate: LocalDate = LocalDate.now(),
    title: String = stringResource(R.string.date_picker_default_title),
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selectedMillis = state.selectedDateMillis
                selectedMillis?.let { epochMillis ->
                    val date = Instant.ofEpochMilli(epochMillis)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()

                    onDismissRequest()
                    onDateChanged(date)
                }
            }) {
                Text(stringResource(R.string.date_picker_set))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.date_picker_cancel))
            }
        },
    ) {
        DatePicker(
            state = state,
            title = {
                Text(
                    title,
                    modifier = Modifier.padding(start = 20.dp, top = 22.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
    }
}
