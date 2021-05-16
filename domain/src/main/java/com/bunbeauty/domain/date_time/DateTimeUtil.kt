package com.bunbeauty.domain.date_time

import org.joda.time.DateTime
import org.joda.time.DateTimeConstants.MONDAY
import org.joda.time.DateTimeConstants.SUNDAY
import javax.inject.Inject

class DateTimeUtil @Inject constructor(): IDateTimeUtil {

    override fun getDateDDMMMMYYYY(millis: Long): String {
        val dateTime = DateTime(millis)

        return dateTime.toString("dd MMMM YYYY")
    }

    override fun getWeekPeriod(millis: Long): String {
        val dateTime = DateTime(millis)
        val weekMonday = getDateDDMMMMYYYY(dateTime.withDayOfWeek(MONDAY).millis)
        val weekSunday =  getDateDDMMMMYYYY(dateTime.withDayOfWeek(SUNDAY).millis)

        return "$weekMonday - $weekSunday"
    }

    override fun getDateMMMMYYYY(millis: Long): String {
        val dateTime = DateTime(millis)

        return MONTH_NAME_LIST[dateTime.monthOfYear - 1] + " " + dateTime.year
    }

    companion object {
        private val MONTH_NAME_LIST = listOf(
            "январь",
            "февраль",
            "март",
            "апрель",
            "май",
            "июнь",
            "июль",
            "август",
            "сентябрь",
            "октябрь",
            "ноябрь",
            "декабрь",
        )
    }
}