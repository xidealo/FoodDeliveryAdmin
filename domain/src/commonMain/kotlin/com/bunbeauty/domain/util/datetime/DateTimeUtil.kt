import com.bunbeauty.domain.util.datetime.IDateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_HH_MM
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_YYYY
import com.bunbeauty.domain.util.datetime.PATTERN_HH_MM
import com.bunbeauty.domain.util.datetime.PATTERN_MMMM
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.time.ExperimentalTime

private const val SECONDS_IN_HOUR = 60 * 60
private const val SECONDS_IN_MINUTE = 60

object DateTimeUtil : IDateTimeUtil {
    val DayOfWeek.daysToMonday: Int
        get() =
            when (this) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> -1
                DayOfWeek.WEDNESDAY -> -2
                DayOfWeek.THURSDAY -> -3
                DayOfWeek.FRIDAY -> -4
                DayOfWeek.SATURDAY -> -5
                DayOfWeek.SUNDAY -> -6
            }

    val DayOfWeek.daysToSunday: Int
        get() =
            when (this) {
                DayOfWeek.MONDAY -> 6
                DayOfWeek.TUESDAY -> 5
                DayOfWeek.WEDNESDAY -> 4
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 2
                DayOfWeek.SATURDAY -> 1
                DayOfWeek.SUNDAY -> 0
            }

    private fun formatTime(localDataTime: LocalDateTime): String = "${localDataTime.hour.pad()}:${localDataTime.minute.pad()}"

    private fun formatMonth(localDataTime: LocalDateTime): String = russianMonths[localDataTime.month.number - 1]

    private fun formatDayMonth(localDataTime: LocalDateTime): String = "${localDataTime.day} ${formatMonth(localDataTime)}"

    private fun formatDayMonthTime(localDataTime: LocalDateTime): String = "${formatDayMonth(localDataTime)} ${formatTime(localDataTime)}"

    private fun formatFullDate(localDataTime: LocalDateTime): String =
        "${localDataTime.day} ${formatMonth(localDataTime)} ${localDataTime.year}"

    private fun Int.pad(): String = toString().padStart(2, '0')

    private val russianMonths =
        listOf(
            "января",
            "февраля",
            "марта",
            "апреля",
            "мая",
            "июня",
            "июля",
            "августа",
            "сентября",
            "октября",
            "ноября",
            "декабря",
        )

    private fun Int.formatAsTimeZoneString(): String {
        val sign = if (this >= 0) "+" else "-"
        val absoluteHours = abs(this).toString().padStart(2, '0')
        return "$sign$absoluteHours:00"
    }

    fun LocalDate.toRussianString(): String {
        val monthName =
            when (month.number) {
                1 -> "января"
                2 -> "февраля"
                3 -> "марта"
                4 -> "апреля"
                5 -> "мая"
                6 -> "июня"
                7 -> "июля"
                8 -> "августа"
                9 -> "сентября"
                10 -> "октября"
                11 -> "ноября"
                12 -> "декабря"
                else -> ""
            }
        return "$day $monthName $year"
    }

    @OptIn(ExperimentalTime::class)
    override fun getWeekPeriod(millis: Long): String {
        val date =
            Instant
                .fromEpochMilliseconds(millis)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        val dayOfWeek = date.dayOfWeek

        val monday = date.plus(dayOfWeek.daysToMonday, DateTimeUnit.DAY)
        val sunday = date.plus(dayOfWeek.daysToSunday, DateTimeUnit.DAY)

        return "${monday.toRussianString()} - ${sunday.toRussianString()}"
    }

    override fun getDaySeconds(time: LocalTime): Int = time.hour * SECONDS_IN_HOUR + time.minute * SECONDS_IN_MINUTE

    override fun getLocalTime(daySeconds: Int): LocalTime {
        val hours = daySeconds / SECONDS_IN_HOUR
        val minutes = daySeconds / SECONDS_IN_MINUTE

        return LocalTime(hours, minutes)
    }

    @OptIn(ExperimentalTime::class)
    override fun formatDateTime(
        millis: Long,
        pattern: String,
        offset: Int?,
    ): String {
        val instant = Instant.fromEpochMilliseconds(millis)
        val timeZone = offset?.toTimeZone() ?: TimeZone.currentSystemDefault()
        val localDateTime = instant.toLocalDateTime(timeZone)
        return when (pattern) {
            PATTERN_HH_MM -> {
                formatTime(localDateTime)
            }

            PATTERN_MMMM -> {
                formatMonth(localDateTime)
            }

            PATTERN_DD_MMMM -> formatDayMonth(localDateTime)
            PATTERN_DD_MMMM_HH_MM -> formatDayMonthTime(localDateTime)
            PATTERN_DD_MMMM_YYYY -> formatFullDate(localDateTime)
            else -> localDateTime.toString()
        }
    }

    override fun getTimeHHMM(daySeconds: Int): String {
        val hours = daySeconds / SECONDS_IN_HOUR
        val minutes = (daySeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE

        return getTimeText(hours, minutes)
    }

    override fun getTimeHHMM(localTime: LocalTime): String {
        val hours = localTime.hour
        val minutes = localTime.minute

        return getTimeText(hours, minutes)
    }

    private fun Int?.toTimeZone(): TimeZone {
        val formattedOffset = this?.formatAsTimeZoneString()
        return if (
            formattedOffset == null
        ) {
            TimeZone.currentSystemDefault()
        } else {
            TimeZone.of(formattedOffset)
        }
    }

    private fun getTimeText(
        hours: Int,
        minutes: Int,
    ): String {
        val minutesString =
            if (minutes < 10) {
                "0$minutes"
            } else {
                minutes.toString()
            }

        return "$hours:$minutesString"
    }
}
