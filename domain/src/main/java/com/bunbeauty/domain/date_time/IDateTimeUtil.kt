package com.bunbeauty.domain.date_time

interface IDateTimeUtil {

    fun getDateDDMMMMYYYY(millis: Long): String
    fun getWeekPeriod(millis: Long): String
    fun getDateMMMMYYYY(millis: Long): String
    fun getDateTimeDDMMHHMM(millis: Long): String

    fun getTimeHHMM(millis: Long): String
}