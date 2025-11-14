package com.bunbeauty.domain.util.datetime

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.MONDAY
import org.joda.time.DateTimeConstants.SUNDAY
import org.joda.time.DateTimeZone
import java.time.LocalTime

private const val SECONDS_IN_MINUTE = 60
private const val MINUTE_IN_HOUR = 60
private const val SECONDS_IN_HOUR = 60 * 60

class DateTimeUtil : IDateTimeUtil {
    override fun getWeekPeriod(millis: Long): String {
        val dateTime = DateTime(millis)
        val mondayDateTimeMillis = dateTime.withDayOfWeek(MONDAY).millis
        val sundayDateTimeMillis = dateTime.withDayOfWeek(SUNDAY).millis
        val formatMondayDateTime = formatDateTime(mondayDateTimeMillis, PATTERN_DD_MMMM_YYYY)
        val formatSundayDateTime = formatDateTime(sundayDateTimeMillis, PATTERN_DD_MMMM_YYYY)

        return "$formatMondayDateTime - $formatSundayDateTime"
    }

    override fun getMillisDaysAgo(days: Int): Double =
        DateTime
            .now()
            .minusDays(days)
            .millis
            .toDouble()

    override fun getDaySeconds(time: LocalTime): Int = time.hour * SECONDS_IN_HOUR + time.minute * SECONDS_IN_MINUTE

    override fun getLocalTime(daySeconds: Int): LocalTime =
        LocalTime.of(
            daySeconds / SECONDS_IN_HOUR,
            daySeconds / SECONDS_IN_MINUTE % MINUTE_IN_HOUR,
        )

    override fun formatDateTime(
        millis: Long,
        pattern: String,
        offset: Int?,
    ): String {
        val dateTime =
            DateTime(millis).let { dateTime ->
                if (offset == null) {
                    dateTime
                } else {
                    dateTime.withZone(DateTimeZone.forOffsetHours(offset))
                }
            }
        return dateTime.toString(pattern)
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
