package rahulstech.android.booknest.ui.component

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateSaver: Saver<LocalDate?, Long> {
    override fun SaverScope.save(value: LocalDate?): Long? =
        value?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    override fun restore(value: Long): LocalDate? =
        Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
}