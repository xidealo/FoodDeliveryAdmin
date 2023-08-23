package com.bunbeauty.domain.util.datetime

interface IDateTimeUtil {

    fun getDateDDMMMMYYYY(millis: Long): String
    fun getWeekPeriod(millis: Long): String
    fun getDateMMMMYYYY(millis: Long): String
    fun getDateTimeDDMMHHMM(millis: Long): String
    fun getMillisDaysAgo(days: Int): Double

    fun getTimeHHMM(millis: Long): String
}